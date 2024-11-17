package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.pvp.model.SpecialItems;
import de.skymyth.user.model.User;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.TimeUnit;

public record PlayerInteractListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemBuilder itemStack = new ItemBuilder(player.getItemInHand());

        if (player.getWorld().getName().equalsIgnoreCase("Spawn")) {
            Block block = event.getClickedBlock();
            if (block == null) return;
            if (block.getType() != Material.ENDER_PORTAL_FRAME) return;

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                User user = plugin.getUserManager().getUser(player.getUniqueId());
                if (user.isOnCooldown("dailyReward")) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst noch " + TimeUtil.beautifyTime(user.getCooldown("dailyReward").getRemainingTime(), TimeUnit.MILLISECONDS, true, true) + " warten.");
                    return;
                }

                plugin.getRewardsManager().openFor(player);
            }
        }

        if (player.getWorld().getName().equalsIgnoreCase("PvP")) {
            if (SpecialItems.FLAME_ROSE.getItemStack().isSimilar(itemStack)) {


            }
        }


    }

}
