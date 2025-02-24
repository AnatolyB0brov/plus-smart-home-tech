package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.hubs.DeviceEvent;
import ru.yandex.practicum.service.DeviceService;

@RestController
@RequestMapping("/events/hubs")
public class HubController {

    private final DeviceService deviceService;

    public HubController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public void collectHubEvent(@Valid @RequestBody DeviceEvent deviceEvent) {
        deviceService.processDeviceEvent(deviceEvent);
    }
}
