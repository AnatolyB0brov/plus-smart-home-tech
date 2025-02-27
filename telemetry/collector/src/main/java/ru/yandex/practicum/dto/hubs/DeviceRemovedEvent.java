package ru.yandex.practicum.dto.hubs;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.dto.hubs.enums.DeviceEventType;

@Getter
@Setter
public class DeviceRemovedEvent extends DeviceEvent {
    private String id;
    @Override
    public DeviceEventType getType() {
        return DeviceEventType.DEVICE_REMOVED;
    }
}
