package ru.yandex.practicum.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.Nullable;
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
    private final KafkaTemplate<String, SpecificRecordBase> producer;
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
        producer.send(topic,1,timestamp,key,specificRecordBase);
    }
}
