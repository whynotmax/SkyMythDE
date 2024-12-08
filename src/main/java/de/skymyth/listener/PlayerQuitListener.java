package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;
import de.skymyth.utility.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public record PlayerQuitListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = plugin.getUserManager().getUser(player.getUniqueId());

        plugin.getPlayerCount().fastPut("skypvp", (Bukkit.getOnlinePlayers().size() - Util.VANISH.size()));

        event.setQuitMessage(null);
        user.updatePlayTime();
        plugin.getScoreboardManager().destroyScoreboard(player);
        Util.VANISH.remove(player);

        if (user.hasQuitMessage()) {
            Bukkit.broadcastMessage("§8[§c-§8] §7" + user.getQuitMessage().replace('&', '§').replace("§k", "&k"));
        }

        if (player.getWorld().getName().equalsIgnoreCase("PvP") || player.getWorld().getName().equalsIgnoreCase("FpsArena")) {
            user.setLastLocation(player.getLocation().clone());
        }

        plugin.getUserManager().saveUser(user);

    }
}
