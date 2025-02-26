package ru.yandex.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.hubs.DeviceEvent;
import ru.yandex.practicum.service.DeviceService;

@RestController
@RequestMapping("/events/hubs")
@AllArgsConstructor
@Slf4j
public class HubController {
    private final DeviceService deviceService;

    @PostMapping
    public void collectHubEvent(@Valid @RequestBody DeviceEvent deviceEvent) {
        log.info("{}",deviceEventString);
        deviceService.processDeviceEvent(deviceEvent);
    }
}
