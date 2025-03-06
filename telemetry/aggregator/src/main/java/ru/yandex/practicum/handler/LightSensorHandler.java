package ru.yandex.practicum.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorHandler extends SensorHandler<LightSensorAvro> {
    @Override
    public Class<LightSensorAvro> getMessageType() {
        return LightSensorAvro.class;
    }
}
