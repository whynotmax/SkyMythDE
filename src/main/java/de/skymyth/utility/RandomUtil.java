package de.skymyth.utility;

import de.skymyth.SkyMythPlugin;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class RandomUtil {

    public static void teleportRandomly(Player player, World world) {
        int minX = -1000;
        int maxX = 1000;
        int minZ = -1000;
        int maxZ = 1000;

        int x = (int) (Math.random() * (maxX - minX + 1) + minX);
        int z = (int) (Math.random() * (maxZ - minZ + 1) + minZ);

        CompletableFuture.supplyAsync(() -> {
            Chunk chunk = world.getChunkAt(x >> 4, z >> 4);
            if (!chunk.isLoaded()) {
                chunk.load();
            }
            return chunk;
        }).thenAccept(chunk -> {
            int y = world.getHighestBlockYAt(x, z);
            player.teleport(world.getBlockAt(x, y, z).getLocation());
            TitleUtil.sendTitle(player, 0, 20, 20, "§aTeleportiert", "§8× §7X: §e" + x + "§8 - §7Z: §e" + z + " §8×");
        }).exceptionally(ex -> {
            ex.printStackTrace(); // Log the exception
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Chunk konnte nicht geladen werden.");
            return null;
        });
    }

}
