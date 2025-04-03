package ru.yandex.practicum.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.ConditionType;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

@Component
public class LightSensorEventHandler implements SensorEventHandler {

    @Override
    public String getSensorType() {
        return LightSensorAvro.class.getName();
    }

    @Override
    public Integer getSensorValue(ConditionType conditionType, SensorStateAvro sensorState) {
        LightSensorAvro lightSensor = (LightSensorAvro) sensorState.getData();
        return switch (conditionType) {
            case LUMINOSITY -> lightSensor.getLuminosity();
            default -> null;
        };
    }
}