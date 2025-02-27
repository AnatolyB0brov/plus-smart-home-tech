package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.sensors.SensorEvent;
import ru.yandex.practicum.service.SensorService;

@RestController
@RequestMapping(path = "/events/sensors")
@RequiredArgsConstructor
public class SensorsController {
    private final SensorService sensorService;

    @PostMapping
    public void collectSensorEvent(@Valid @RequestBody SensorEvent sensorEvent) {
        sensorService.processSensorEvent(sensorEvent);
    }
}
