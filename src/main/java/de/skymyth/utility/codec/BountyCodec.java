package de.skymyth.utility.codec;

import de.skymyth.bounties.model.Bounty;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BountyCodec implements Codec<Bounty> {
    @Override
    public Bounty decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();
        UUID target = UUID.fromString(bsonReader.readString("target"));
        Map<UUID, Long> hunters = new HashMap<>();
        bsonReader.readStartArray();
        while (bsonReader.readBsonType() != null) {
            bsonReader.readStartDocument();
            UUID hunter = UUID.fromString(bsonReader.readString("hunter"));
            long reward = bsonReader.readInt64("reward");
            bsonReader.readEndDocument();
            hunters.put(hunter, reward);
        }
        bsonReader.readEndDocument();
        return new Bounty(target, hunters);
    }

    @Override
    public void encode(BsonWriter bsonWriter, Bounty bounty, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeString("target", bounty.getTarget().toString());
        bsonWriter.writeStartArray("hunters");
        for (Map.Entry<UUID, Long> entry : bounty.getHunters().entrySet()) {
            bsonWriter.writeStartDocument();
            bsonWriter.writeString("hunter", entry.getKey().toString());
            bsonWriter.writeInt64("reward", entry.getValue());
            bsonWriter.writeEndDocument();
        }
        bsonWriter.writeEndArray();
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<Bounty> getEncoderClass() {
        return Bounty.class;
    }
}
