package ru.yandex.practicum.dto.hubs;

import ru.yandex.practicum.dto.hubs.enums.DeviceEventType;

public class DeviceRemovedEvent extends DeviceEvent {
    @Override
    public DeviceEventType getType() {
        return DeviceEventType.DEVICE_REMOVED;
    }
}
