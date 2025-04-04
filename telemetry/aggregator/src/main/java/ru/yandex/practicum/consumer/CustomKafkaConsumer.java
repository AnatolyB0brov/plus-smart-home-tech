package ru.yandex.practicum.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.handler.SnapshotHandler;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomKafkaConsumer {
    private final SnapshotHandler snapshotHandler;
    private final Consumer<String, SensorEventAvro> consumer;
    private final KafkaConfig config;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    public void startSensorEventsConsume() {

        try {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(config.getKafkaProperties().getSensorEventsTopic()));
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer
                        .poll(Duration.ofMillis(config.getKafkaProperties().getConsumeAttemptTimeout()));
                int count = 0;
                records.forEach(record -> {
                    snapshotHandler.handleConsumerRecord(record);
                    manageOffsets(record, count, consumer);
                });
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    snapshotHandler.handleConsumerRecord(record);
                    manageOffsets(record, count, consumer);
                }
                consumer.commitAsync();
            }

        } catch (WakeupException e) {
            log.error("WakeupException {}", e.getMessage());
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков {}", e.getMessage());
        } finally {
            try {
                consumer.commitSync(currentOffsets);
            } finally {
                consumer.close();
                snapshotHandler.closeProducer();
            }
        }
    }

    private void manageOffsets(ConsumerRecord<String, SensorEventAvro> consumerRecord,
                               int count, Consumer<String, SensorEventAvro> consumer) {
        currentOffsets.put(
                new TopicPartition(consumerRecord.topic(), consumerRecord.partition()),
                new OffsetAndMetadata(consumerRecord.offset() + 1)
        );
        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }
}
