package ru.yandex.practicum.dto.hubs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.dto.hubs.enums.DeviceEventType;
import ru.yandex.practicum.dto.hubs.enums.DeviceType;

@Getter
@Setter
@ToString
public class DeviceAddedEvent extends DeviceEvent {
    private String id;
    private DeviceType deviceType;

    @Override
    public DeviceEventType getType() {
        return DeviceEventType.DEVICE_ADDED;
    }
}
