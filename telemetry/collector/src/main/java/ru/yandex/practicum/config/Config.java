package ru.yandex.practicum.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@Getter
@Setter
@ConfigurationProperties(prefix = "config")
public class Config {
    private String sensorEventsTopic;
    private String hubEventsTopic;
}
