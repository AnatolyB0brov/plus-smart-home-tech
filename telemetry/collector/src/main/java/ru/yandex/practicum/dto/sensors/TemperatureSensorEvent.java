package ru.yandex.practicum.dto.sensors;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.dto.sensors.enums.SensorEventType;

@Getter
@Setter
public class TemperatureSensorEvent extends SensorEvent {
    private Integer temperatureC;
    private Integer temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
