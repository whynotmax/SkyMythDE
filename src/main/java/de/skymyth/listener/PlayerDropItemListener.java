package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public record PlayerDropItemListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!Util.canDropItems) {
            event.setCancelled(true);
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst aktuell keine Items auf den Boden werfen.");
        }
    }

}
