package ru.yandex.practicum.dto.hubs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.dto.hubs.enums.ConditionOperation;
import ru.yandex.practicum.dto.hubs.enums.ConditionType;

@Getter
@Setter
public class ScenarioCondition {

    @NotBlank
    private String sensorId;

    @NotNull
    private ConditionType conditionType;

    @NotNull
    private ConditionOperation conditionOperation;

    @NotNull
    private Integer value;
}
