package ru.yandex.practicum.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.Arrays;

@RestController
@RequestMapping("/events/hubs")
@AllArgsConstructor
@Slf4j
public class HubController {
    private final DeviceService deviceService;

    @PostMapping
    public void collectHubEvent(@RequestBody String deviceEventString) {
        log.info(deviceEventString);
        ObjectMapper objectMapper = new ObjectMapper();
        DeviceEvent deviceEvent  = null;
        try {
            deviceEvent = objectMapper.readValue(deviceEventString, DeviceEvent.class);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        deviceService.processDeviceEvent(deviceEvent);
    }
}
