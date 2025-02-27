package ru.yandex.practicum.dto.hubs;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.dto.hubs.enums.DeviceEventType;

@Getter
@Setter
public class ScenarioRemovedEvent extends DeviceEvent {
    private String name;

    @Override
    public DeviceEventType getType() {
        return DeviceEventType.SCENARIO_REMOVED;
    }
}
