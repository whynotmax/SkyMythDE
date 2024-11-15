package de.skymyth.utility;

import com.mongodb.internal.VisibleForTesting;
import de.skymyth.SkyMythPlugin;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class RandomUtil {

    public static void teleportRandomly(Player player, World world) {
        int minX = -1000;
        int minZ = -1000;
        int maxX = 1000;
        int maxZ = 1000;

        int x = (int) (Math.random() * (maxX - minX + 1) + minX);
        int z = (int) (Math.random() * (maxZ - minZ + 1) + minZ);
        int y = world.getHighestBlockYAt(x, z);

        CompletableFuture.supplyAsync(() -> {
            Chunk chunk = world.getChunkAt(x >> 4, z >> 4);
            if (!chunk.isLoaded()) {
                return chunk.load();
            }
            return true;
        }).thenAccept(loaded -> {
            if (loaded) {
                player.teleport(world.getBlockAt(x, y, z).getLocation());
                TitleUtil.sendTitle(player, 0, 20, 20, "§aTeleportiert", "§8× §7Du wurdest zufällig teleportiert. §8×");
                return;
            }
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Chunk konnte nicht geladen werden.");
        });
    }

}
