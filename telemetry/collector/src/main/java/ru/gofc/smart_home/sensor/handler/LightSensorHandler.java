package ru.gofc.smart_home.sensor.handler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.gofc.smart_home.sensor.kafka.SensorProducer;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LightSensorHandler implements SensorHandler {
    final SensorProducer producer;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto eventProto) {
        LightSensorProto lightSensorProto = eventProto.getLightSensorEvent();

        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(eventProto.getId())
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds()))
                .setPayload(LightSensorAvro.newBuilder()
                        .setLinkQuality(lightSensorProto.getLinkQuality())
                        .setLuminosity(lightSensorProto.getLuminosity())
                        .build()
                )
                .build();

        producer.sendMessage(eventAvro);
    }
}
