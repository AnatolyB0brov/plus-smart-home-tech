package ru.gofc.smart_home.snapshot.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class SnapshotProducer {
    final Producer<String, SensorsSnapshotAvro> producer;
    final String topic;

    public void sendMessage(SensorsSnapshotAvro eventAvro) {
        log.info("Отправление снапшота хаба " + eventAvro.getHubId());
        log.debug("Отправление снапшота хаба " + eventAvro);

        ProducerRecord<String, SensorsSnapshotAvro> producerRecord = new ProducerRecord<>(topic, eventAvro);

        producer.send(producerRecord);
        producer.flush();

        log.info("Успешно отправлен снапшот хаба " + eventAvro.getHubId());
    }

    public void flush() {
        producer.flush();
    }

    public void close() {
        producer.close();
    }
}
