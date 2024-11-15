package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;
import de.skymyth.user.model.cooldown.Cooldown;
import de.skymyth.utility.TimeUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public record PlayerInteractListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onInteract(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase("Spawn")) return;
        event.setCancelled(true);
        Block block = event.getBlock();
        if (block == null) return;
        if (block.getType() != Material.ENDER_PORTAL_FRAME) return;

        User user = plugin.getUserManager().getUser(player.getUniqueId());
        if (user.isOnCooldown("dailyReward")) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst noch " + TimeUtil.beautifyTime(user.getCooldown("dailyReward").getRemainingTime(), TimeUnit.MILLISECONDS, true, true) + " warten.");
            return;
        }

        plugin.getRewardsManager().openFor(player);
    }

}
