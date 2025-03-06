package ru.gofc.smart_home.sensor.kafka;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Slf4j
public class SensorProducer {
    Producer<String, SensorEventAvro> producer;
    final String topic;

    public void sendMessage(SensorEventAvro eventAvro) {
        log.info("Отправление события сенсора " + eventAvro.getId());
        log.debug("Отправление события сенсора " + eventAvro);

        ProducerRecord<String, SensorEventAvro> producerRecord = new ProducerRecord<>(topic, eventAvro);

        producer.send(producerRecord);
        producer.flush();

        log.info("Успешно отправлено событие сенсора " + eventAvro.getId());
    }
}
