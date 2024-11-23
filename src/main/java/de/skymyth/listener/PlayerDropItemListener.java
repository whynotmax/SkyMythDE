package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.Util;
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
        if (world.getName().equalsIgnoreCase("Spawn") && !player.isOp()) {
            event.setCancelled(true);
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst am Spawn keine Items auf den Boden werfen.");
            player.sendMessage(SkyMythPlugin.PREFIX + "§cWenn du etwas wegwerfen möchtest, benutze /müll.");
            return;
        }
        if (!Util.CANDROPITEMS && !player.isOp()) {
            event.setCancelled(true);
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst aktuell keine Items auf den Boden werfen.");
        }
    }

}
