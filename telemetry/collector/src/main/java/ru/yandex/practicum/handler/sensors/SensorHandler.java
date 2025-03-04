package ru.yandex.practicum.handler.sensors;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorHandler {
    SensorEventProto.PayloadCase getMessageType();

    void handle(SensorEventProto eventProto);
}
