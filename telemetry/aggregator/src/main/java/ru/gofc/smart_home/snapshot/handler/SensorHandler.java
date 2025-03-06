package ru.gofc.smart_home.snapshot.handler;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

public abstract class SensorHandler<T> {
    public abstract Class<T> getMessageType();

    Optional<SensorsSnapshotAvro> handle(SensorEventAvro eventAvro, SensorsSnapshotAvro snapshot) {
        T event = (T) eventAvro.getPayload();

        Map<String, SensorStateAvro> states = snapshot.getSensorsState();
        SensorStateAvro deviceSnapshot = states.get(eventAvro.getId());
        T oldEvent = (T) deviceSnapshot.getData();

        if (eventAvro.getTimestamp().isBefore(deviceSnapshot.getTimestamp())) {
            return Optional.empty();
        }
        if (event.equals(oldEvent)) {
            return Optional.empty();
        }

        deviceSnapshot.setData(event);
        states.put(eventAvro.getId(), deviceSnapshot);
        snapshot.setSensorsState(states);
        snapshot.setTimestamp(Instant.now());

        return Optional.of(snapshot);
    }
}
