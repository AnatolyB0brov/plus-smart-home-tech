package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.hubs.DeviceEvent;
import ru.yandex.practicum.kafka.producer.KafkaProducer;

@Service
@AllArgsConstructor
public class DeviceService {
    private final KafkaProducer kafkaProducer;

    public void processDeviceEvent(DeviceEvent deviceEvent) {
        kafkaProducer.sendDeviceEvent(deviceEvent);
    }
}
