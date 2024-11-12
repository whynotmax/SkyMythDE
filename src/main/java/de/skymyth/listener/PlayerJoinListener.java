package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public record PlayerJoinListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.getUserManager().loadUser(player.getUniqueId());

        event.setJoinMessage(null);
        plugin.getScoreboardManager().createScoreboard(player);

        for (int i = 0; i < 50; i++) {
            player.sendMessage("§" + String.valueOf(i).charAt(0));
        }

        player.sendMessage("");

        if (!player.hasPlayedBefore()) {

        }


    }
}
