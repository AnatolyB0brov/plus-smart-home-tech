package ru.yandex.practicum.handler.sensors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.producer.KafkaProducer;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
@AllArgsConstructor
public class LightSensorHandler implements SensorHandler {
    private final KafkaProducer producer;

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
                .setTimestamp(eventProto.getTimestamp().getSeconds())
                .setPayload(LightSensorAvro.newBuilder()
                        .setLinkQuality(lightSensorProto.getLinkQuality())
                        .setLuminosity(lightSensorProto.getLuminosity())
                        .build()
                )
                .build();

        producer.sendSensorEventAvro(eventAvro);
    }
}
