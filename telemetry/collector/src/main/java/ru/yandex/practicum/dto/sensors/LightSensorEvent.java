package ru.yandex.practicum.dto.sensors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.dto.sensors.enums.SensorEventType;

@Getter
@Setter
@ToString
public class LightSensorEvent extends SensorEvent {
    private Integer linkQuality;
    private Integer luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
