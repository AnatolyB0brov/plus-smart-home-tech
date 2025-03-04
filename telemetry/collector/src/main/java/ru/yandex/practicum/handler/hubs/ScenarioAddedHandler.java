package ru.yandex.practicum.handler.hubs;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.producer.KafkaProducer;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ScenarioAddedHandler implements HubHandler {

    private final KafkaProducer producer;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEventProto eventProto) {
        ScenarioAddedEventProto scenarioAddedEventProto = eventProto.getScenarioAdded();

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(eventProto.getTimestamp().getSeconds())
                .setPayload(
                        ScenarioAddedEventAvro.newBuilder()
                                .setName(scenarioAddedEventProto.getName())
                                .setConditions(
                                        scenarioAddedEventProto.getConditionList().stream()
                                                .map(this::mapScenarioCondition)
                                                .collect(Collectors.toList())
                                )
                                .setActions(
                                        scenarioAddedEventProto.getActionList().stream()
                                                .map(this::mapDeviceAction)
                                                .collect(Collectors.toList())
                                )
                                .build()
                )
                .build();

        producer.sendHubEventAvro(eventAvro);
    }

    private ScenarioConditionAvro mapScenarioCondition(ScenarioConditionProto scenarioConditionProto) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioConditionProto.getSensorId())
                .setValue(scenarioConditionProto.getBoolValue())
                .setOperation(ConditionOperationAvro.valueOf(scenarioConditionProto.getOperation().name()))
                .setType(ConditionTypeAvro.valueOf(scenarioConditionProto.getType().name()))
                .build();
    }

    private DeviceActionAvro mapDeviceAction(DeviceActionProto deviceActionProto) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceActionProto.getSensorId())
                .setValue(deviceActionProto.getValue())
                .setType(ActionTypeAvro.valueOf(deviceActionProto.getType().name()))
                .build();
    }
}
