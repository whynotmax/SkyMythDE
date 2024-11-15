package de.skymyth.utility;

import de.skymyth.SkyMythPlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class RandomUtil {

    private static final Set<Material> UNSAFE_MATERIALS = new HashSet<>();
    public static final int RADIUS = 3;
    public static final Vector3D[] VOLUME;

    public static Location safeizeLocation(Location location) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = (int)location.getY();
        int z = location.getBlockZ();
        int origX = x;
        int origY = y;
        int origZ = z;
        location.setY((double)location.getWorld().getHighestBlockYAt(location));

        while(isBlockAboveAir(world, x, y, z)) {
            --y;
            if (y < 0) {
                y = origY;
                break;
            }
        }

        if (isBlockUnsafe(world, x, y, z)) {
            x = Math.round(location.getX()) == (long)x ? x - 1 : x + 1;
            z = Math.round(location.getZ()) == (long)z ? z - 1 : z + 1;
        }

        for(int i = 0; isBlockUnsafe(world, x, y, z); z = origZ + VOLUME[i].z) {
            ++i;
            if (i >= VOLUME.length) {
                x = origX;
                y = origY + 3;
                z = origZ;
                break;
            }

            x = origX + VOLUME[i].x;
            y = origY + VOLUME[i].y;
        }

        while(isBlockUnsafe(world, x, y, z)) {
            ++y;
            if (y >= world.getMaxHeight()) {
                ++x;
                break;
            }
        }

        while(isBlockUnsafe(world, x, y, z)) {
            --y;
            if (y <= 1) {
                ++x;
                y = world.getHighestBlockYAt(x, z);
                if (x - 48 > location.getBlockX()) {
                    return null;
                }
            }
        }

        return new Location(world, (double)x + 0.5D, (double)y, (double)z + 0.5D, location.getYaw(), location.getPitch());
    }

    public static boolean isBlockAboveAir(World world, int x, int y, int z) {
        return y > world.getMaxHeight() || !world.getBlockAt(x, y - 1, z).getType().isSolid();
    }

    public static boolean isBlockUnsafe(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        Block below = world.getBlockAt(x, y - 1, z);
        Block above = world.getBlockAt(x, y + 1, z);
        return UNSAFE_MATERIALS.contains(below.getType()) || block.getType().isSolid() || above.getType().isSolid() || isBlockAboveAir(world, x, y, z);
    }

    static {
        UNSAFE_MATERIALS.add(Material.LAVA);
        UNSAFE_MATERIALS.add(Material.STATIONARY_LAVA);
        UNSAFE_MATERIALS.add(Material.FIRE);
        List<Vector3D> pos = new ArrayList<>();

        for(int x = -3; x <= 3; ++x) {
            for(int y = -3; y <= 3; ++y) {
                for(int z = -3; z <= 3; ++z) {
                    pos.add(new Vector3D(x, y, z));
                }
            }
        }

        Collections.sort(pos, Comparator.comparingInt(a -> a.x * a.x + a.y * a.y + a.z * a.z));
        VOLUME = pos.toArray(new Vector3D[0]);
    }

    public static int randomInt(int i, int i1) {
        return new Random().nextInt(i1 - i) + i;
    }

    public static class Vector3D {
        public int x;
        public int y;
        public int z;

        public Vector3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

}
