package ru.gofc.smart_home.hub.kafka;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HubEventSerializer implements Serializer<HubEventAvro> {
    @Override
    public byte[] serialize(String s, HubEventAvro event) {
        if (event == null) {
            return null;
        }

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(stream, null);
            DatumWriter<HubEventAvro> writer = new SpecificDatumWriter<>(HubEventAvro.class);

            writer.write(event, encoder);
            encoder.flush();

            return stream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Ошибка сериализации SensorEvent", e);
        }
    }
}
