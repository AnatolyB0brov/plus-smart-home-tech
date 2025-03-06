package ru.yandex.practicum.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "kafka.config")
public class KafkaConfigProperties {
    String bootstrapServers;
    String producerClientIdConfig;
    String producerKeySerializer;
    String producerValueSerializer;
    String consumerGroupId;
    String consumerClientIdConfig;
    String consumerKeyDeserializer;
    String consumerValueDeserializer;
    long consumeAttemptTimeout;
    String sensorEventsTopic;
    String sensorSnapshotsTopic;
}