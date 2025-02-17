package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.baseprotector.model.BaseProtector;
import de.skymyth.utility.Util;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public record BlockPlaceListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();

        if (world.getName().equals("world")) {

            double distanceToSpawn = event.getBlock().getLocation().distance(plugin.getLocationManager().getPosition("Farmwelt").getLocation());
            if (Math.round(distanceToSpawn) < 500 && !player.isOp()) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Du kannst erst in §e" + (500 - Math.round(distanceToSpawn)) + " §7Blöcken platzieren.");
                event.setCancelled(true);
                return;
            }

            if (plugin.getBaseProtectorManager().isBlockProtected(event.getBlock())) {
                BaseProtector baseProtector = plugin.getBaseProtectorManager().getBaseProtection(event.getBlock());

                if (event.getBlock().getType() == Material.ENDER_PORTAL_FRAME) {
                    event.setCancelled(true);
                    return;
                }

                if (player.isOp()) {
                    return;
                }

                if (!baseProtector.getTrustedPlayers().contains(player.getUniqueId()) &&
                        !baseProtector.getBaseOwner().equals(player.getUniqueId())) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDie Base von " +
                            Bukkit.getOfflinePlayer(baseProtector.getBaseOwner()).getName() + " §cist geschützt.");
                    event.setCancelled(true);
                }
                return;
            }

            ItemStack itemStack = event.getItemInHand();
            if (itemStack.getItemMeta().getDisplayName() != null &&
                    itemStack.getItemMeta().getDisplayName().equals("§cBasisschutz")
                    && itemStack.getType() == Material.ENDER_PORTAL_FRAME) {

                if (plugin.getBaseProtectorManager().hasBaseProtection(player.getUniqueId())) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst nicht mehr als einen Basisschutz haben.");
                    event.setCancelled(true);
                    return;
                }

                if (!plugin.getBaseProtectorManager().isBaseSpotFine(event.getBlockPlaced().getLocation())) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst hier keinen Basisschutz platzieren.");
                    event.setCancelled(true);
                    return;
                }

                Util.removeItem(player, itemStack);
                plugin.getBaseProtectorManager().createBaseProtection(player.getUniqueId(), event.getBlockPlaced().getLocation());
                player.sendMessage(SkyMythPlugin.PREFIX + "§aDein Basisschutz wurde erfolgreich erstellt.");
                player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 1, 1);
                for (int i = 0; i < 256; i++) {
                    event.getBlockPlaced().getWorld().playEffect(event.getBlockPlaced().getLocation()
                            .add(0.5f, i, 0.5f), Effect.COLOURED_DUST, i * 3);
                }


            }
        }

        if (!player.isOp()) {
            if (world.getName().equals("Spawn") || world.getName().equals("PvP") || world.getName().equalsIgnoreCase("FpsArena")) {
                event.setCancelled(true);
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDazu hast du keine Rechte.");
            }
        }

    }

}
