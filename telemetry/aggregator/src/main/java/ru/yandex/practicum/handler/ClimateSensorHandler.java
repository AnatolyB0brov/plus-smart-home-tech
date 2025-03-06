package ru.yandex.practicum.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorHandler extends SensorHandler<ClimateSensorAvro> {
    @Override
    public Class<ClimateSensorAvro> getMessageType() {
        return ClimateSensorAvro.class;
    }
}
