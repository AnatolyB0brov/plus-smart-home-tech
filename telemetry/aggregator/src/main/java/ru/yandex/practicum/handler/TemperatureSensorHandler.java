package ru.yandex.practicum.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorHandler extends SensorHandler<TemperatureSensorAvro> {
    @Override
    public Class<TemperatureSensorAvro> getMessageType() {
        return TemperatureSensorAvro.class;
    }
}
