package ru.yandex.practicum.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.Config;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, SpecificRecordBase> producer;
    private final Config config;

    public void sendSensorEventAvro(SensorEventAvro sensorEventAvro) {
        send(config.getSensorEventsTopic(),
                sensorEventAvro.getHubId(),
                sensorEventAvro.getTimestamp().getEpochSecond(),
                sensorEventAvro);
    }

    public void sendHubEventAvro(HubEventAvro hubEventAvro) {
        send(config.getHubEventsTopic(),
                hubEventAvro.getHubId(),
                hubEventAvro.getTimestamp().getEpochSecond(),
                hubEventAvro);
    }

    private void send(String topic, String key, Long timestamp, SpecificRecordBase specificRecordBase) {
        log.info("Sending event to topic: {}, key: {}, timestamp: {}, specific {}", topic, key, timestamp,
                specificRecordBase);
        producer.send(topic, null, timestamp, key, specificRecordBase);
    }
}
