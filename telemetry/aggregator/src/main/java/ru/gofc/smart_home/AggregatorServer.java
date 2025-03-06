package ru.gofc.smart_home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.gofc.smart_home.snapshot.AggregatorStarter;

@SpringBootApplication
public class AggregatorServer {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AggregatorServer.class, args);

        AggregatorStarter aggregator = context.getBean(AggregatorStarter.class);
        aggregator.start();
    }
}
