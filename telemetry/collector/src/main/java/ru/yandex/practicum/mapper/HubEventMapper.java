package ru.yandex.practicum.mapper;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.dto.hubs.*;
import ru.yandex.practicum.dto.hubs.enums.ConditionOperation;
import ru.yandex.practicum.dto.hubs.enums.ConditionType;
import ru.yandex.practicum.dto.hubs.enums.DeviceActionType;
import ru.yandex.practicum.dto.hubs.enums.DeviceType;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.Objects;

public class HubEventMapper {

    public static HubEventAvro toHubEventAvro(DeviceEvent deviceEvent) {
        return HubEventAvro.newBuilder()
                .setHubId(deviceEvent.getHubId())
                .setTimestamp(deviceEvent.getTimestamp().toEpochMilli())
                .setPayload(toHubEventPayloadAvro(deviceEvent))
                .build();
    }

    public static SpecificRecordBase toHubEventPayloadAvro(DeviceEvent deviceEvent) {
        Objects.requireNonNull(deviceEvent.getType(), "Hub event type cannot be null");

        return switch (deviceEvent.getType()) {
            case DEVICE_ADDED -> mapDeviceAdded((DeviceAddedEvent) deviceEvent);
            case DEVICE_REMOVED -> mapDeviceRemoved((DeviceRemovedEvent) deviceEvent);
            case SCENARIO_ADDED -> mapScenarioAdded((ScenarioAddedEvent) deviceEvent);
            case SCENARIO_REMOVED -> mapScenarioRemoved((ScenarioRemovedEvent) deviceEvent);
            default -> throw new IllegalStateException("Invalid payload: " + deviceEvent.getType());
        };
    }

    private static DeviceAddedEventAvro mapDeviceAdded(DeviceAddedEvent event) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setType(toDeviceTypeAvro(event.getDeviceType()))
                .build();
    }

    private static DeviceRemovedEventAvro mapDeviceRemoved(DeviceRemovedEvent event) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();
    }

    private static ScenarioAddedEventAvro mapScenarioAdded(ScenarioAddedEvent event) {
        return ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setActions(event.getActions().stream().map(HubEventMapper::toDeviceActionAvro).toList())
                .setConditions(event.getConditions().stream().map(HubEventMapper::toScenarioConditionAvro).toList())
                .build();
    }

    private static ScenarioRemovedEventAvro mapScenarioRemoved(ScenarioRemovedEvent event) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();
    }

    public static DeviceTypeAvro toDeviceTypeAvro(DeviceType deviceType) {
        return DeviceTypeAvro.valueOf(deviceType.name());
    }

    public static DeviceActionAvro toDeviceActionAvro(DeviceAction deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(toActionTypeAvro(deviceAction.getType()))
                .setValue(deviceAction.getValue())
                .build();
    }

    public static ActionTypeAvro toActionTypeAvro(DeviceActionType deviceActionType) {
        return ActionTypeAvro.valueOf(deviceActionType.name());
    }

    public static ConditionTypeAvro toConditionTypeAvro(ConditionType conditionType) {
        return ConditionTypeAvro.valueOf(conditionType.name());
    }

    public static ConditionOperationAvro toConditionOperationAvro(ConditionOperation conditionOperation) {
        return ConditionOperationAvro.valueOf(conditionOperation.name());
    }

    public static ScenarioConditionAvro toScenarioConditionAvro(ScenarioCondition scenarioCondition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setType(toConditionTypeAvro(scenarioCondition.getConditionType()))
                .setOperation(toConditionOperationAvro(scenarioCondition.getConditionOperation()))
                .setValue(scenarioCondition.getValue())
                .build();
    }
}
