package ru.yandex.practicum.dto.sensors;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.dto.sensors.enums.SensorEventType;

@Getter
@Setter
public class ClimateSensorEvent extends SensorEvent{
    private Integer temperatureC;
    private Integer humidity;
    private Integer co2Level;
    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

}
