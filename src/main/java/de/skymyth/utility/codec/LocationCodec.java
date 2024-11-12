package de.skymyth.utility.codec;

import lombok.extern.java.Log;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@Log
public class LocationCodec implements Codec<Location> {

    @Override
    public Location decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        double x = reader.readDouble("x");
        double y = reader.readDouble("y");
        double z = reader.readDouble("z");
        float yaw = (float) reader.readDouble("yaw");
        float pitch = (float) reader.readDouble("pitch");
        String worldString = reader.readString("world");
        reader.readEndDocument();

        World world = Bukkit.getWorld(worldString);
        if (world == null) {
            return null;
        }

        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public void encode(BsonWriter writer, Location location, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeDouble("x", location.getX());
        writer.writeDouble("y", location.getY());
        writer.writeDouble("z", location.getZ());
        writer.writeDouble("yaw", location.getYaw());
        writer.writeDouble("pitch", location.getPitch());
        writer.writeString("world", location.getWorld().getName());
        writer.writeEndDocument();
    }

    @Override
    public Class<Location> getEncoderClass() {
        return Location.class;
    }
}
