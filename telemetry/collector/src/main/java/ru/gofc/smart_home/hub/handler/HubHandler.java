package ru.gofc.smart_home.hub.handler;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubHandler {
    HubEventProto.PayloadCase getMessageType();
     void handle(HubEventProto eventProto);
}
