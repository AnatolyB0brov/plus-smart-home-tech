package ru.yandex.practicum.handler.sensors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.producer.KafkaProducer;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
@AllArgsConstructor
public class ClimateSensorHandler implements SensorHandler {
    private final KafkaProducer producer;

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
                .setTimestamp(eventProto.getTimestamp().getSeconds())
                .setPayload(ClimateSensorAvro.newBuilder()
                        .setHumidity(climateSensorProto.getHumidity())
                        .setCo2Level(climateSensorProto.getCo2Level())
                        .setTemperatureC(climateSensorProto.getTemperatureC())
                        .build()
                )
                .build();

        producer.sendSensorEventAvro(eventAvro);
    }
}
