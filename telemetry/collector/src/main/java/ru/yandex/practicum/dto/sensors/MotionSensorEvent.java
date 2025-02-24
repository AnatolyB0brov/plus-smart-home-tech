package ru.yandex.practicum.dto.sensors;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.dto.sensors.enums.SensorEventType;

@Getter
@Setter
public class MotionSensorEvent extends SensorEvent {
    private Integer linkQuality;
    private Boolean motion;
    private Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
