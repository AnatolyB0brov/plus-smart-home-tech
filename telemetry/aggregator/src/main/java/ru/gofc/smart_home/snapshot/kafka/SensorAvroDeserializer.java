package ru.gofc.smart_home.snapshot.kafka;

import org.apache.avro.Schema;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public class SensorAvroDeserializer extends BaseAvroDeserializer<SensorEventAvro> {
    public SensorAvroDeserializer(Schema schema) {
        super(schema);
    }
}
