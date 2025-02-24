package ru.yandex.practicum.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.config.Config;
import ru.yandex.practicum.dto.hubs.DeviceEvent;
import ru.yandex.practicum.dto.sensors.SensorEvent;
import ru.yandex.practicum.mapper.HubEventMapper;
import ru.yandex.practicum.mapper.SensorEventMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final Producer<String, SpecificRecordBase> producer;
    private final Config config;

    public void sendSensorEvent(SensorEvent sensorEvent) {
        send(config.getSensorEventsTopic(),
                sensorEvent.getHubId(),
                sensorEvent.getTimestamp().toEpochMilli(),
                SensorEventMapper.toSensorEventAvro(sensorEvent));
    }

    public void sendDeviceEvent(DeviceEvent deviceEvent) {
        send(config.getHubEventsTopic(),
                deviceEvent.getHubId(),
                deviceEvent.getTimestamp().toEpochMilli(),
                HubEventMapper.toHubEventAvro(deviceEvent));
    }

    private void send(String topic, String key, Long timestamp, SpecificRecordBase specificRecordBase) {
        log.info("Sending event to topic: {}, key: {}, timestamp: {}", topic, key, timestamp);
        producer.send(new ProducerRecord<>(topic, null, timestamp, key, specificRecordBase),
                (metadata, exception) -> {
                    if (exception != null) {
                        log.error("Failed to send event to topic {}: {}", topic, exception.getMessage());
                    } else {
                        log.info("Event sent successfully to topic {} at partition {}, offset {}",
                                topic, metadata.partition(), metadata.offset());
                    }
                });
    }
}
