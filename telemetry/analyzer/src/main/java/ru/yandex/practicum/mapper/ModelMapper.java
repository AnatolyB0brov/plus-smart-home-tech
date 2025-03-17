package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@UtilityClass
public class ModelMapper {

    public Sensor mapToSensor(HubEventAvro hubEventAvro, DeviceAddedEventAvro deviceAddedEventAvro) {
        return new Sensor(
                deviceAddedEventAvro.getId(),
                hubEventAvro.getHubId()
        );
    }

    public Scenario mapToScenario(HubEventAvro hubEventAvro, ScenarioAddedEventAvro scenarioAddedEventAvro) {
        Scenario scenario = new Scenario();
        scenario.setHubId(hubEventAvro.getHubId());
        scenario.setName(scenarioAddedEventAvro.getName());
        scenario.setConditions(scenarioAddedEventAvro.getConditions().stream()
                .map(conditionAvro -> mapToCondition(scenario, conditionAvro))
                .toList());
        scenario.setActions(scenarioAddedEventAvro.getActions().stream()
                .map(actionAvro -> mapToAction(scenario, actionAvro))
                .toList());

        return scenario;
    }

    public Condition mapToCondition(Scenario scenario, ScenarioConditionAvro conditionAvro) {
        return Condition.builder()
                .sensor(new Sensor(conditionAvro.getSensorId(), scenario.getHubId()))
                .type(toConditionType(conditionAvro.getType()))
                .operation(toConditionOperation(conditionAvro.getOperation()))
                .value(getConditionValue(conditionAvro.getValue()))
                .scenarios(List.of(scenario))
                .build();
    }

    public Action mapToAction(Scenario scenario, DeviceActionAvro deviceActionAvro) {
        return Action.builder()
                .sensor(new Sensor(deviceActionAvro.getSensorId(), scenario.getHubId()))
                .type(toActionType(deviceActionAvro.getType()))
                .value(deviceActionAvro.getValue())
                .build();
    }

    public ConditionType toConditionType(ConditionTypeAvro conditionTypeAvro) {
        return ConditionType.valueOf(conditionTypeAvro.name());
    }

    public ConditionOperation toConditionOperation(ConditionOperationAvro conditionOperationAvro) {
        return ConditionOperation.valueOf(conditionOperationAvro.name());
    }

    public ActionType toActionType(ActionTypeAvro actionTypeAvro) {
        return ActionType.valueOf(actionTypeAvro.name());
    }

    public Integer getConditionValue(Object conditionValue) {
        if (conditionValue == null) {
            return null;
        }
        if (conditionValue instanceof Boolean) {
            return ((Boolean) conditionValue ? 1 : 0);
        }
        if (conditionValue instanceof Integer) {
            return (Integer) conditionValue;
        }
        throw new ClassCastException("Ошибка преобразования значения");
    }
}
