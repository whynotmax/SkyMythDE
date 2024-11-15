package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public record PlayerQuitListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = plugin.getUserManager().getUser(player.getUniqueId());

        event.setQuitMessage(null);
        user.updatePlayTime();
        plugin.getScoreboardManager().destroyScoreboard(player);

        plugin.getUserManager().saveUser(user);


    }
}
