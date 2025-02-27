package ru.yandex.practicum.dto.sensors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.dto.sensors.enums.SensorEventType;

@Getter
@Setter
@ToString
public class SwitchSensorEvent extends SensorEvent {
    private Boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
