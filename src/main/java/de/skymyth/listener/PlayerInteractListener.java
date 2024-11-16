package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.kit.ui.KitMainInventory;
import de.skymyth.protector.ui.ProtectorMainInventory;
import de.skymyth.user.model.User;
import de.skymyth.utility.TimeUtil;
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


        if(player.getWorld().getName().equalsIgnoreCase("world")) {


            if(plugin.getProtectorManager().isBlockInsideOfProtector(event.getClickedBlock())) {
                if(event.getClickedBlock().getType() == Material.ENDER_PORTAL_FRAME) {
                    plugin.getInventoryManager().openInventory(player, new ProtectorMainInventory(this.plugin));
                }
            }

            if(event.getItem() != null && event.getItem().hasItemMeta() && event.getItem().getItemMeta().getDisplayName().equals("§8» §aBasisschutz")) {

                if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    if(plugin.getProtectorManager().hasProtector(player.getUniqueId())) {
                        player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst nicht mehr als einen Basisschutz platzieren.");
                        event.setCancelled(true);
                        return;
                    }

                    plugin.getProtectorManager().createProtector(player.getUniqueId(), event.getClickedBlock().getLocation());
                    player.sendMessage(SkyMythPlugin.PREFIX + "§aDein Basisschutz ist jetzt aktiv!");
                    player.getInventory().remove(event.getItem());
                }

            }

        }
    }

}
