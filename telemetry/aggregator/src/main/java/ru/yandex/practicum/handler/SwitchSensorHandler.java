package ru.yandex.practicum.handler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorHandler extends SensorHandler<SwitchSensorAvro> {
    @Override
    public Class<SwitchSensorAvro> getMessageType() {
        return SwitchSensorAvro.class;
    }
}
