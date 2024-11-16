package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public record PlayerRespawnListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        plugin.getRewardsManager().getHologram().hide(event.getPlayer());
        plugin.getCasinoManager().getDailyPotManager().getHologram().hide(event.getPlayer());
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getRewardsManager().getHologram().show(event.getPlayer(), 1);
            plugin.getCasinoManager().getDailyPotManager().getHologram().show(event.getPlayer(), 1);
        }, 10L);
    }

}
