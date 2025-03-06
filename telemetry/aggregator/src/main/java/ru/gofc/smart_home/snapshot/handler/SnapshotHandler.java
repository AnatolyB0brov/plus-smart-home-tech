package ru.gofc.smart_home.snapshot.handler;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SnapshotHandler {
    final Map<String, SensorsSnapshotAvro> snapshots;
    final Map<Class<? extends SpecificRecord>, SensorHandler<? extends SpecificRecord>> handlers;

    public SnapshotHandler(List<SensorHandler<? extends SpecificRecord>> handlers) {
        snapshots = new HashMap<>();
        this.handlers = new HashMap<>();
        for (SensorHandler<? extends SpecificRecord> handler: handlers) {
            this.handlers.put(handler.getMessageType(), handler);
        }
    }

    public Optional<SensorsSnapshotAvro> handleKafkaMessage(SensorEventAvro eventAvro) {
        String hubId = eventAvro.getHubId();
        log.info("Начата агрегация данных хаба " + hubId);

        SensorsSnapshotAvro snapshot;
        if (snapshots.containsKey(hubId)) {
            snapshot = snapshots.get(hubId);
        } else {
            snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(hubId)
                    .setTimestamp(Instant.now())
                    .setSensorsState(new HashMap<>())
                    .build();
        }

        if (!handlers.containsKey(eventAvro.getPayload().getClass())) {
            return Optional.empty();
        } else {
            Optional<SensorsSnapshotAvro> result = handlers.get(eventAvro.getPayload().getClass())
                    .handle(eventAvro, snapshot);

            result.ifPresent(sensorsSnapshotAvro -> snapshots.put(hubId, sensorsSnapshotAvro));

            return result;
        }
    }
}
