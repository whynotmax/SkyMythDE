package de.skymyth.utility.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.Code;

import java.time.Duration;

public class DurationCodec implements Codec<Duration> {
    @Override
    public Duration decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();
        long millis = bsonReader.readInt64("millis");
        bsonReader.readEndDocument();
        return Duration.ofMillis(millis);
    }

    @Override
    public void encode(BsonWriter bsonWriter, Duration duration, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeInt64("millis", duration.toMillis());
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<Duration> getEncoderClass() {
        return Duration.class;
    }
}
