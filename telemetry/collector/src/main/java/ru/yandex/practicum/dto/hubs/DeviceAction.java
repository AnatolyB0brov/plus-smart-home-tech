package ru.yandex.practicum.dto.hubs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.dto.hubs.enums.DeviceActionType;

@Getter
@Setter
@ToString
public class DeviceAction {
    @NotBlank
    private String sensorId;

    @NotNull
    private DeviceActionType type;

    private Integer value;
}
