package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.dto.sensors.SensorEvent;
import ru.yandex.practicum.service.SensorService;

@Controller
@RequestMapping(path = "/events/sensors")
@RequiredArgsConstructor
public class SensorsController {
    private final SensorService sensorService;

    @PostMapping
    public void collectSensorEvent(@RequestBody SensorEvent sensorEvent) {
        sensorService.processSensorEvent(sensorEvent);
    }
}
