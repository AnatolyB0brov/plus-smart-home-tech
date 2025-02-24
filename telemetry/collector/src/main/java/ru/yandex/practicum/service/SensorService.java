package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.sensors.SensorEvent;
import ru.yandex.practicum.kafka.producer.KafkaProducer;

@Service
@AllArgsConstructor
public class SensorService {

    private final KafkaProducer kafkaProducer;

    public void processSensorEvent(SensorEvent event) {
        kafkaProducer.sendSensorEvent(event);
    }
}
