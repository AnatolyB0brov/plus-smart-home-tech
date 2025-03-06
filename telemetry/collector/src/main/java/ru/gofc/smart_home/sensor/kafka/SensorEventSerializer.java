package ru.gofc.smart_home.sensor.kafka;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SensorEventSerializer implements Serializer<SensorEventAvro> {
    @Override
    public byte[] serialize(String s, SensorEventAvro event) {
        if (event == null) {
            return null;
        }

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(stream, null);
            DatumWriter<SensorEventAvro> writer = new SpecificDatumWriter<>(SensorEventAvro.class);

            writer.write(event, encoder);
            encoder.flush();

            return stream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Ошибка сериализации SensorEvent", e);
        }
    }
}
