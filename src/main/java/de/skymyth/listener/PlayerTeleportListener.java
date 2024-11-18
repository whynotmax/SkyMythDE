package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public record PlayerTeleportListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onPlayerTeleportFromTo(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        World fromWorld = event.getFrom().getWorld();
        World toWorld = event.getTo().getWorld();
        if (!fromWorld.getName().equalsIgnoreCase(toWorld.getName())) {
            plugin.getScoreboardManager().updateScoreboard(player);
        }
    }

}
