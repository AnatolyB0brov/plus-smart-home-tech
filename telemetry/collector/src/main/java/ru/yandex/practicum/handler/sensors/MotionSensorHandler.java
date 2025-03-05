package ru.yandex.practicum.handler.sensors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.producer.KafkaProducer;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component
@AllArgsConstructor
public class MotionSensorHandler implements SensorHandler {
    private final KafkaProducer producer;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto eventProto) {
        MotionSensorEvent motionSensorEvent = eventProto.getMotionSensorEvent();

        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(eventProto.getId())
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds()))
                .setPayload(MotionSensorAvro.newBuilder()
                        .setMotion(motionSensorEvent.getMotion())
                        .setLinkQuality(motionSensorEvent.getLinkQuality())
                        .setVoltage(motionSensorEvent.getVoltage())
                        .build()
                )
                .build();

        producer.sendSensorEventAvro(eventAvro);
    }
}
