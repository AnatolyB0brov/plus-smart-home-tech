package ru.yandex.practicum.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final Producer<String, SpecificRecordBase> producer;
    private final KafkaConfig config;

    public void sendSensorEventAvro(SensorEventAvro sensorEventAvro) {
        send(config.getKafkaProperties().getSensorEventsTopic(),
                sensorEventAvro.getHubId(),
                sensorEventAvro.getTimestamp().getEpochSecond(),
                sensorEventAvro);
    }

    public void sendHubEventAvro(HubEventAvro hubEventAvro) {
        send(config.getKafkaProperties().getHubEventsTopic(),
                hubEventAvro.getHubId(),
                hubEventAvro.getTimestamp().getEpochSecond(),
                hubEventAvro);
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
}
