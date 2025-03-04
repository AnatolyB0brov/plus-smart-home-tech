package ru.yandex.practicum.handler.sensors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.producer.KafkaProducer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
@AllArgsConstructor
public class SwitchSensorHandler implements SensorHandler {
    private final KafkaProducer producer;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto eventProto) {
        SwitchSensorProto switchSensorProto = eventProto.getSwitchSensorEvent();

        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(eventProto.getId())
                .setHubId(eventProto.getHubId())
                .setTimestamp(eventProto.getTimestamp().getSeconds())
                .setPayload(SwitchSensorAvro.newBuilder()
                        .setState(switchSensorProto.getState())
                        .build()
                )
                .build();

        producer.sendSensorEventAvro(eventAvro);
    }
}
