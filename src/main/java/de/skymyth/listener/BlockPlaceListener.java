package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.Util;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import javax.lang.model.element.ElementVisitor;

public record BlockPlaceListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();

        if (world.getName().equals("world")) {

            ItemBuilder itemBuilder = new ItemBuilder(event.getItemInHand());
            if (event.getItemInHand().getType() == Material.ENDER_PORTAL_FRAME) {

                if (plugin.getBaseProtectorManager().hasBaseProtection(player.getUniqueId())) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst nicht mehr als einen Basisschutz haben.");
                    event.setCancelled(true);
                    return;
                }

                Util.removeItem(player, itemBuilder);
                plugin.getBaseProtectorManager().createBaseProtection(player.getUniqueId(), event.getBlockPlaced().getLocation());
                player.sendMessage(SkyMythPlugin.PREFIX + "§aDein Basisschutz wurde erfolgreich erstellt.");
            }
        }

        if (!player.isOp()) {
            if (world.getName().equals("Spawn") || world.getName().equals("PvP")) {
                event.setCancelled(true);
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDazu hast du keine Rechte.");
            }
        }

    }

}
