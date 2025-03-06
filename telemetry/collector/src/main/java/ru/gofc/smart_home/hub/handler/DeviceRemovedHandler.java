package ru.gofc.smart_home.hub.handler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.gofc.smart_home.hub.kafka.HubProducer;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceRemovedHandler implements HubHandler {
    final HubProducer producer;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    public void handle(HubEventProto eventProto) {
        DeviceRemovedEventProto deviceRemovedEventProto = eventProto.getDeviceRemoved();

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds()))
                .setPayload(
                        DeviceRemovedEventAvro.newBuilder()
                                .setId(deviceRemovedEventProto.getId())
                                .build()
                )
                .build();

        producer.sendMessage(eventAvro);
    }
}
