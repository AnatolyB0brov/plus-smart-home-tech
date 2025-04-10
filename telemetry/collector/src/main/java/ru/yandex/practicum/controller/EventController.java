package ru.yandex.practicum.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.handler.hubs.HubHandler;
import ru.yandex.practicum.handler.sensors.SensorHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@GrpcService
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final Map<SensorEventProto.PayloadCase, SensorHandler> sensorEventHandlers;
    private final Map<HubEventProto.PayloadCase, HubHandler> hubEventHandlers;

    public EventController(Set<SensorHandler> sensorHandlers, Set<HubHandler> hubHandlers) {
        sensorEventHandlers = sensorHandlers.stream()
                .collect(Collectors.toMap(
                        SensorHandler::getMessageType,
                        Function.identity()
                ));
        hubEventHandlers = hubHandlers.stream()
                .collect(Collectors.toMap(
                        HubHandler::getMessageType,
                        Function.identity()
                ));
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("HubEventProto request {}", request);
        try {
            if (!hubEventHandlers.containsKey(request.getPayloadCase())) {
                throw new IllegalArgumentException("Hub event handler not found" + request.getPayloadCase());
            }
            hubEventHandlers.get(request.getPayloadCase()).handle(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            if (!sensorEventHandlers.containsKey(request.getPayloadCase())) {
                throw new IllegalArgumentException("Sensor event handler not found " + request.getPayloadCase());
            }
            sensorEventHandlers.get(request.getPayloadCase()).handle(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }
}
