package ru.yandex.practicum.dto.hubs;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.dto.hubs.enums.DeviceEventType;

import java.util.List;

@Getter
@Setter
public class ScenarioAddedEvent extends DeviceEvent {
    private String name;
    private List<ScenarioCondition> conditions;
    private List<DeviceAction> actions;

    @Override
    public DeviceEventType getType() {
        return DeviceEventType.SCENARIO_ADDED;
    }
}
