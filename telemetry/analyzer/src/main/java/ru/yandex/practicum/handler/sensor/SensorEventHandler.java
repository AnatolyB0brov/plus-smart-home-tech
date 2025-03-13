package ru.yandex.practicum.handler.sensor;

import ru.yandex.practicum.model.ConditionType;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

public interface SensorEventHandler {

    String getSensorType();

    Integer getSensorValue(ConditionType conditionType, SensorStateAvro sensorState);
}