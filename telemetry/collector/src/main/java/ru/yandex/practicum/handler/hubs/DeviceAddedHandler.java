package ru.yandex.practicum.handler.hubs;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.producer.KafkaProducer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
@AllArgsConstructor
public class DeviceAddedHandler implements HubHandler {
    private final KafkaProducer producer;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEventProto eventProto) {
        DeviceAddedEventProto deviceAddedEventProto = eventProto.getDeviceAdded();

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(eventProto.getTimestamp().getSeconds())
                .setPayload(
                        DeviceAddedEventAvro.newBuilder()
                                .setId(deviceAddedEventProto.getId())
                                .setType(DeviceTypeAvro.valueOf(deviceAddedEventProto.getType().name()))
                                .build()
                )
                .build();

        producer.sendHubEventAvro(eventAvro);
    }
}
