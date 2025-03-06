package ru.gofc.smart_home.snapshot.config;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.gofc.smart_home.snapshot.AggregatorStarter;
import ru.gofc.smart_home.snapshot.handler.SnapshotHandler;
import ru.gofc.smart_home.snapshot.kafka.SensorAvroDeserializer;
import ru.gofc.smart_home.snapshot.kafka.SnapshotProducer;
import ru.gofc.smart_home.snapshot.kafka.SnapshotSerializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.List;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.yaml")
@AllArgsConstructor
public class SnapshotKafkaConfig {
    @Value("${kafka.constants.url}")
    private final String url;
    @Value("${kafka.constants.sensor.topic}")
    private final String readTopic;
    @Value("${kafka.constants.snapshot.topic}")
    private final String writeTopic;

    @Bean
    public AggregatorStarter aggregatorStarter(SnapshotProducer producer, SnapshotHandler handler) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "snapshot");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorAvroDeserializer.class);

        Consumer<String, SensorEventAvro> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(List.of(readTopic));

        return new AggregatorStarter(consumer, producer, handler);
    }

    @Bean
    public SnapshotProducer snapshotProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, url);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SnapshotSerializer.class);

        Producer<String, SensorsSnapshotAvro> producer = new KafkaProducer<>(properties);

        return new SnapshotProducer(producer, writeTopic);
    }
}
