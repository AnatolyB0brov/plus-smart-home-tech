package ru.yandex.practicum.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final Producer<String, SpecificRecordBase> producer;
    private final KafkaConfig config;

    public void sendSensorsSnapshotAvro(SensorsSnapshotAvro sensorsSnapshotAvro) {
        send(config.getKafkaProperties().getSensorEventsTopic(),
                sensorsSnapshotAvro.getHubId(),
                sensorsSnapshotAvro.getTimestamp().getEpochSecond(),
                sensorsSnapshotAvro);
    }

    private void send(String topic, String key, Long timestamp, SpecificRecordBase specificRecordBase) {
        log.info("Sending specificRecordBase {} to topic {}", specificRecordBase, topic);
        ProducerRecord<String, SpecificRecordBase> rec = new ProducerRecord<>(
                topic,
                null,
                timestamp,
                key,
                specificRecordBase);
        producer.send(rec);
    }

    public void flush() {
        producer.flush();
    }

    public void close() {
        producer.close();
    }
}
