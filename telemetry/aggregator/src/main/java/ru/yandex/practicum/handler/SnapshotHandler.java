package ru.yandex.practicum.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.producer.CustomKafkaProducer;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class SnapshotHandler {
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();
    private final CustomKafkaProducer producer;

    public void handleConsumerRecord(ConsumerRecord<String, SensorEventAvro> consumerRecord) {
        Optional<SensorsSnapshotAvro> snapshotAvro = updateState(consumerRecord.value());
        snapshotAvro.ifPresent(producer::sendSensorsSnapshotAvro);
    }

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro snapshotAvro = snapshots.computeIfAbsent(
                event.getHubId(),
                this::getNewSensorsSnapshotAvro
        );

        SensorStateAvro oldState = snapshotAvro.getSensorsState().get(event.getId());
        if (oldState != null && (oldState.getTimestamp().isAfter(event.getTimestamp())
                || oldState.getData().equals(event.getPayload()))) {
            return Optional.empty();
        }

        SensorStateAvro newState = getNewSensorsSnapshotAvro(event);
        snapshotAvro.getSensorsState().put(event.getId(), newState);
        snapshotAvro.setTimestamp(event.getTimestamp());
        snapshots.put(event.getHubId(), snapshotAvro);
        return Optional.of(snapshotAvro);
    }

    private SensorsSnapshotAvro getNewSensorsSnapshotAvro(String key) {
        return SensorsSnapshotAvro.newBuilder()
                .setHubId(key)
                .setTimestamp(Instant.now())
                .setSensorsState(new HashMap<>())
                .build();
    }

    private SensorStateAvro getNewSensorsSnapshotAvro(SensorEventAvro event) {
        return SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();
    }
}
