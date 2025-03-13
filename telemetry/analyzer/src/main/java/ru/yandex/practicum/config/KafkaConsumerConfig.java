package ru.yandex.practicum.config;

import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Getter
@Configuration
@EnableConfigurationProperties(KafkaConfigProperties.class)
public class KafkaConsumerConfig {

    private final KafkaConfigProperties kafkaConfigProperties;

    public KafkaConsumerConfig(KafkaConfigProperties kafkaConfigProperties) {
        this.kafkaConfigProperties = kafkaConfigProperties;
    }

    @Bean
    public KafkaConsumer<String, SensorsSnapshotAvro> sensorsSnapshotConsumer() {
        Properties props = createConsumerProperties(kafkaConfigProperties.getSnapshotConsumer());
        return new KafkaConsumer<>(props);
    }

    @Bean
    public KafkaConsumer<String, HubEventAvro> hubEventConsumer() {
        Properties props = createConsumerProperties(kafkaConfigProperties.getHubConsumer());
        return new KafkaConsumer<>(props);
    }

    private Properties createConsumerProperties(ConsumerProperties consumerProperties) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProperties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.getGroupId());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerProperties.getClientId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumerProperties.getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumerProperties.getValueDeserializer());
        return props;
    }
}