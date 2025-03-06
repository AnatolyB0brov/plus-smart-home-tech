package ru.gofc.smart_home.sensor.handler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.gofc.smart_home.sensor.kafka.SensorProducer;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClimateSensorHandler implements SensorHandler {
    final SensorProducer producer;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto eventProto) {
        ClimateSensorProto climateSensorProto = eventProto.getClimateSensorEvent();

        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(eventProto.getId())
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds()))
                .setPayload(ClimateSensorAvro.newBuilder()
                        .setHumidity(climateSensorProto.getHumidity())
                        .setCo2Level(climateSensorProto.getCo2Level())
                        .setTemperatureC(climateSensorProto.getTemperatureC())
                        .build()
                )
                .build();

        producer.sendMessage(eventAvro);
    }
}
