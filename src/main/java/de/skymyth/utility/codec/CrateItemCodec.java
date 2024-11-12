package de.skymyth.utility.codec;

import de.skymyth.crate.model.CrateItem;
import de.skymyth.utility.item.ItemStackConverter;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.ArrayList;
import java.util.List;

public class CrateItemCodec implements Codec<CrateItem> {

    @Override
    public CrateItem decode(BsonReader bsonReader, DecoderContext decoderContext) {
        CrateItem crateItem = new CrateItem();
        bsonReader.readStartDocument();
        crateItem.setItemStack(ItemStackConverter.decode(bsonReader.readString("itemStack")));
        crateItem.setChance(bsonReader.readDouble("chance"));
        crateItem.setBroadcast(bsonReader.readBoolean("broadcast"));
        List<String> commands = new ArrayList<>();
        bsonReader.readStartArray();
        while (bsonReader.readBsonType() != org.bson.BsonType.END_OF_DOCUMENT) {
            commands.add(bsonReader.readString());
        }
        bsonReader.readEndArray();
        crateItem.setCommands(commands);
        crateItem.setTokens(bsonReader.readInt64("tokens"));
        bsonReader.readEndDocument();
        return crateItem;
    }

    @Override
    public void encode(BsonWriter bsonWriter, CrateItem crateItem, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeString("itemStack", ItemStackConverter.encode(crateItem.getItemStack()));
        bsonWriter.writeDouble("chance", crateItem.getChance());
        bsonWriter.writeBoolean("broadcast", crateItem.isBroadcast());
        bsonWriter.writeStartArray("commands");
        for (String command : crateItem.getCommands()) {
            bsonWriter.writeString(command);
        }
        bsonWriter.writeEndArray();
        bsonWriter.writeInt64("tokens", crateItem.getTokens());
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<CrateItem> getEncoderClass() {
        return CrateItem.class;
    }
}
