package ru.gofc.smart_home.hub.config;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.gofc.smart_home.hub.kafka.HubEventSerializer;
import ru.gofc.smart_home.hub.kafka.HubProducer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.Properties;

@Configuration
@PropertySource("classpath:application.yaml")
@AllArgsConstructor
public class HubKafkaConfig {
    @Value("${kafka.constants.url}")
    private final String url;
    @Value("${kafka.constants.hub.topic}")
    private final String topic;

    @Bean
    public HubProducer hubProducerConfig() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, url);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, HubEventSerializer.class);

        Producer<String, HubEventAvro> producer = new KafkaProducer<>(properties);

        return new HubProducer(producer, topic);
    }
}
