package ru.yandex.practicum.handler.hubs;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.producer.KafkaProducer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Component
@AllArgsConstructor
public class DeviceRemovedHandler implements HubHandler {
    private final KafkaProducer producer;

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

        producer.sendHubEventAvro(eventAvro);
    }
}
