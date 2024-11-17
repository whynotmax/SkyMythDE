package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public record PlayerRespawnListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {

        Player player = event.getPlayer();

        player.teleport(plugin.getLocationManager().getPosition("spawn").getLocation());

        plugin.getRewardsManager().getHologram().hide(player);
        plugin.getCasinoManager().getDailyPotManager().getHologram().hide(player);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getRewardsManager().getHologram().show(player, 1);
            plugin.getCasinoManager().getDailyPotManager().getHologram().show(player, 1);
        }, 10L);
    }

}
