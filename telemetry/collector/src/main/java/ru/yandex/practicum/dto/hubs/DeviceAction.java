package ru.yandex.practicum.dto.hubs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.dto.hubs.enums.DeviceActionType;

@Getter
@Setter
@ToString
public class DeviceAction {
    private String sensorId;
    private DeviceActionType type;
    private Integer value;
}
