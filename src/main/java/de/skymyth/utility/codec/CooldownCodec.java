package de.skymyth.utility.codec;

import de.skymyth.user.model.cooldown.Cooldown;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.Duration;

public class CooldownCodec implements Codec<Cooldown> {
    @Override
    public Cooldown decode(BsonReader bsonReader, DecoderContext decoderContext) {
        String name;
        Duration duration;
        long start;
        bsonReader.readStartDocument();
        name = bsonReader.readString("name");
        duration = Duration.ofMillis(bsonReader.readInt64("duration"));
        start = bsonReader.readInt64("start");
        bsonReader.readEndDocument();
        Cooldown cooldown = new Cooldown();
        cooldown.setName(name);
        cooldown.setDuration(duration);
        cooldown.setStart(start);
        return cooldown;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Cooldown cooldown, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeString("name", cooldown.getName());
        bsonWriter.writeInt64("duration", cooldown.getDuration().toMillis());
        bsonWriter.writeInt64("start", cooldown.getStart());
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<Cooldown> getEncoderClass() {
        return Cooldown.class;
    }
}
