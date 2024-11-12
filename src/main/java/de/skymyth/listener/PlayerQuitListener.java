package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public record PlayerQuitListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.setQuitMessage(null);

        plugin.getScoreboardManager().destroyScoreboard(player);


        //Immer zum Schluss
        plugin.getUserManager().saveUser(player.getUniqueId());

    }
}
