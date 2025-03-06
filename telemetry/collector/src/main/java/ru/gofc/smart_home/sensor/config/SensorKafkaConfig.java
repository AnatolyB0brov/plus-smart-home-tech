package ru.gofc.smart_home.sensor.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.gofc.smart_home.sensor.kafka.SensorProducer;
import ru.gofc.smart_home.sensor.kafka.SensorEventSerializer;

import java.util.Properties;

@Configuration
@PropertySource("classpath:application.yaml")
@RequiredArgsConstructor
public class SensorKafkaConfig {
    @Value("${kafka.constants.url}")
    private String url;
    @Value("${kafka.constants.sensor.topic}")
    private String topic;

    @Bean
    public SensorProducer sensorProducerConfig() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, url);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SensorEventSerializer.class);

        Producer<String, SensorEventAvro> producer = new KafkaProducer<>(properties);

        return new SensorProducer(producer, topic);
    }
}
