package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;
import de.skymyth.utility.TitleUtil;
import de.skymyth.utility.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public record PlayerPortalListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onPlayerTeleport(PlayerPortalEvent event) {

        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock();

        if (player.getWorld().getName().equals("Spawn")) {
            if (block.getType().equals(Material.ENDER_PORTAL)) {
                Location location = plugin.getRandomPvPLocations().get(Util.RANDOM.nextInt(plugin.getRandomPvPLocations().size()));
                player.teleport(location);
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 2.0F);
                TitleUtil.sendTitle(player, 0, 30, 20, "§c§lPvP", "§8× §7Zufällig teleportiert §8×");

                User user = plugin.getUserManager().getUser(player.getUniqueId());
                user.setPvpShards(0L);
                plugin.getUserManager().saveUser(user);
                plugin.getScoreboardManager().updateScoreboard(player);
            }
        }
    }
}
