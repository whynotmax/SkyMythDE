package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

public record PlayerAchievementAwardedListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onPlayerAchievementAwardedEvent(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }
}
