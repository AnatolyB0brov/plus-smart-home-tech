package ru.yandex.practicum.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaConsumerConfig;
import ru.yandex.practicum.service.HubEventService;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.utils.CustomUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubConsumer implements Runnable {
    private final Consumer<String, HubEventAvro> consumer;
    private final KafkaConsumerConfig kafkaConsumerConfig;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private final HubEventService service;

    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        try {
            consumer.subscribe(List.of(kafkaConsumerConfig.getKafkaConfigProperties().getHubEventsTopic()));
            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer
                        .poll(Duration.ofMillis(kafkaConsumerConfig.getKafkaConfigProperties()
                                .getHubConsumer().getAttemptTimeout()));
                int count = 0;
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    handleRecord(record);
                    CustomUtils.manageOffsets(record, count, consumer, currentOffsets);
                    count++;
                }
                consumer.commitAsync();
            }

        } catch (WakeupException e) {
            log.error("WakeupException ", e);
        } catch (Exception e) {
            log.error("Ошибка во время обработки события хаба ", e);
        } finally {

            try {
                consumer.commitSync(currentOffsets);

            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
            }
        }
    }

    private void handleRecord(ConsumerRecord<String, HubEventAvro> consumerRecord) {
        log.info("handleRecord {}", consumerRecord);
        service.process(consumerRecord.value());
    }
}
