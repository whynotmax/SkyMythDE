package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.baseprotector.model.BaseProtector;
import de.skymyth.baseprotector.ui.BaseMainInventory;
import de.skymyth.user.model.User;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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

        if (player.getWorld().getName().equals("Spawn")) {
            if(player.getItemInHand() != null) {
                if(player.getItemInHand().getType() == Material.BOW || player.getItemInHand().getType() == Material.ENDER_PEARL && !player.isOp()) {
                    event.setUseItemInHand(Event.Result.DENY);
                    event.setCancelled(true);
                }
            }

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

        if (player.getWorld().getName().equals("world")) {

            Block block = event.getClickedBlock();

            if (block != null && event.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.ENDER_PORTAL_FRAME) {
                BaseProtector baseProtector = plugin.getBaseProtectorManager().getBaseProtection(block);

                if (baseProtector != null) {
                    if (baseProtector.getBaseOwner().equals(player.getUniqueId())) {
                        plugin.getInventoryManager().openInventory(player, new BaseMainInventory(player, plugin));
                    }
                }
            }

        }
    }

}
