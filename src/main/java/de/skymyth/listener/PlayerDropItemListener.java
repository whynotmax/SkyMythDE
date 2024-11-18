package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.Util;
import net.minecraft.server.v1_8_R3.WorldGenAcaciaTree;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public record PlayerDropItemListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (world.getName().equalsIgnoreCase("Spawn")) {
            event.setCancelled(true);
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst am Spawn keine Items auf den Boden werfen.");
            player.sendMessage(SkyMythPlugin.PREFIX + "§cWenn du etwas wegwerfen möchtest, benutze /müll.");
            return;
        }
        if (!Util.canDropItems) {
            event.setCancelled(true);
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst aktuell keine Items auf den Boden werfen.");
        }
    }

}
