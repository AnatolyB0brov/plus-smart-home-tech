package ru.yandex.practicum.starter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.handler.SnapshotHandler;
import ru.yandex.practicum.producer.KafkaProducer;

import java.time.Duration;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Component
@Slf4j
public class AggregatorStarter {
    final Consumer<String, SensorEventAvro> consumer;
    final KafkaProducer producer;
    final SnapshotHandler handler;

    public void start() {
        try {
            log.info("Получение данных");

            ConsumerRecords<String, SensorEventAvro> records = consumer.poll(Duration.ofMillis(500));

            for (ConsumerRecord<String, SensorEventAvro> record: records) {
                Optional<SensorsSnapshotAvro> sensorsSnapshotAvro = handler.handleKafkaMessage(record.value());
                sensorsSnapshotAvro.ifPresent(producer::sendSensorsSnapshotAvro);
            }
        } catch (WakeupException e) {

        } catch (Exception e) {
            log.error("Сбой обработки события сенсора", e);
        } finally {
            try {
                producer.flush();
                consumer.commitSync();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }
}
