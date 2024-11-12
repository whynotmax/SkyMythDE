package de.skymyth.utility.codec;

import de.skymyth.utility.ItemStackConverter;
import lombok.extern.java.Log;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bukkit.inventory.ItemStack;

@Log
public class ItemStackCodec implements Codec<ItemStack> {

    @Override
    public ItemStack decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        String base64 = reader.readString("item");
        reader.readEndDocument();
        ItemStack itemStack = ItemStackConverter.decode(base64);
        if (itemStack == null) {
            return null;
        }
        return itemStack;
    }

    @Override
    public void encode(BsonWriter writer, ItemStack itemStack, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("item", ItemStackConverter.encode(itemStack));
        writer.writeEndDocument();
    }

    @Override
    public Class<ItemStack> getEncoderClass() {
        return ItemStack.class;
    }
}
