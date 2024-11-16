package de.skymyth.utility.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

public class ChunkCodec implements Codec<Chunk> {
    @Override
    public Chunk decode(BsonReader bsonReader, DecoderContext decoderContext) {
        int x, z;
        String world;
        Chunk chunk;
        bsonReader.readStartDocument();
        x = bsonReader.readInt32("x");
        z = bsonReader.readInt32("z");
        world = bsonReader.readString("world");
        bsonReader.readEndDocument();
        chunk = Bukkit.getWorld(world).getChunkAt(x, z);
        return chunk;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Chunk chunk, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeInt32("x", chunk.getX());
        bsonWriter.writeInt32("z", chunk.getZ());
        bsonWriter.writeString("world", chunk.getWorld().getName());
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<Chunk> getEncoderClass() {
        return Chunk.class;
    }
}
