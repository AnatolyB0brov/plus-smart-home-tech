package ru.gofc.smart_home.hub.kafka;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Slf4j
public class HubProducer {
    Producer<String, HubEventAvro> producer;
    final String topic;

    public void sendMessage(HubEventAvro event) {
        log.info("Отправление события хаба " + event.getHubId());
        log.debug("Отправление события хаба " + event);

        ProducerRecord<String, HubEventAvro> producerRecord = new ProducerRecord<>(topic, event);

        producer.send(producerRecord);
        producer.flush();

        log.info("Успешно завершилось событие хаба " + event.getHubId());
    }
}
