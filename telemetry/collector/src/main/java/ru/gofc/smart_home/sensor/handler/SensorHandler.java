package ru.gofc.smart_home.sensor.handler;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorHandler {
    SensorEventProto.PayloadCase getMessageType();

    void handle(SensorEventProto eventProto);
}
