package ru.yandex.practicum.handler.hubs;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.producer.KafkaProducer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.time.Instant;

@Component
@AllArgsConstructor
public class ScenarioRemovedHandler implements HubHandler {
    private final KafkaProducer producer;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    public void handle(HubEventProto eventProto) {
        ScenarioRemovedEventProto scenarioRemovedEventProto = eventProto.getScenarioRemoved();

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds(),
                        eventProto.getTimestamp().getNanos()))
                .setPayload(
                        ScenarioRemovedEventAvro.newBuilder()
                                .setName(scenarioRemovedEventProto.getName())
                                .build()
                )
                .build();

        producer.sendHubEventAvro(eventAvro);
    }
}
