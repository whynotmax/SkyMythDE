package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.baseprotector.model.BaseProtector;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public record BlockBreakListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();

        /*
        if (plugin.getLocationManager().getPosition("test").getLocation().distance(event.getBlock().getLocation()) < 5) {
            event.setCancelled(true);
            player.sendMessage("§cDistance to test -" + plugin.getLocationManager().getPosition("test").getLocation().distance(event.getBlock().getLocation()));
        }

         */

        if (player.getWorld().getName().equals("world")) {

            if (plugin.getBaseProtectorManager().isBlockProtected(event.getBlock())) {
                BaseProtector baseProtector = plugin.getBaseProtectorManager().getBaseProtection(event.getBlock());

                if (event.getBlock().getType() == Material.ENDER_PORTAL_FRAME) event.setCancelled(true);

                if (!baseProtector.getBaseOwner().equals(player.getUniqueId()) || baseProtector.getTrustedPlayers().contains(player.getUniqueId())) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Die Base von §e" + Bukkit.getOfflinePlayer(baseProtector.getBaseOwner()).getName() + " §7ist §cgeschützt.");
                    event.setCancelled(true);
                    return;
                }
                return;
            }

        }


        if (!player.isOp()) {
            if (world.getName().equalsIgnoreCase("Spawn") || world.getName().equalsIgnoreCase("PvP")) {
                event.setCancelled(true);
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDazu hast du keine Rechte.");
            }
        }
    }


}
