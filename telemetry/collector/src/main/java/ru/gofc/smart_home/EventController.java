package ru.gofc.smart_home;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import io.nity.grpc.server.GrpcService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.gofc.smart_home.hub.handler.HubHandler;
import ru.gofc.smart_home.sensor.handler.SensorHandler;
import ru.yandex.practicum.grpc.telemetry.controller.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrpcService
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {
    final Map<SensorEventProto.PayloadCase, SensorHandler> sensorEventHandlers;
    final Map<HubEventProto.PayloadCase, HubHandler> hubEventHandlers;

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
        try {
            if (hubEventHandlers.containsKey(request.getPayloadCase())) {
                hubEventHandlers.get(request.getPayloadCase()).handle(request);
            } else {
                throw new IllegalArgumentException("Не найден обработчик события хаба " + request.getPayloadCase());
            }

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
            if (sensorEventHandlers.containsKey(request.getPayloadCase())) {
                sensorEventHandlers.get(request.getPayloadCase()).handle(request);
            } else {
                throw new IllegalArgumentException("Не найден обработчик для события сенсора " + request.getPayloadCase());
            }

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
