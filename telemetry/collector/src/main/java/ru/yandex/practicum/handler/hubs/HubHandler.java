package ru.yandex.practicum.handler.hubs;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubHandler {
    HubEventProto.PayloadCase getMessageType();
     void handle(HubEventProto eventProto);
}
