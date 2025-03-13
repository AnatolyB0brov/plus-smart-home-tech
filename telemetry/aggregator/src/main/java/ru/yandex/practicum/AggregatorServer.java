package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.consumer.CustomKafkaConsumer;

@SpringBootApplication
public class AggregatorServer {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AggregatorServer.class, args);
        CustomKafkaConsumer consumer = context.getBean(CustomKafkaConsumer.class);
        consumer.startSensorEventsConsume();
    }
}
