package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.Util;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public record EntityDamageListener(SkyMythPlugin plugin) implements Listener {


    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {


        if (event.getEntity() instanceof Player player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (player.getWorld().getName().equalsIgnoreCase("Spawn") || player.getWorld().getName().equalsIgnoreCase("PvP")) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {

        World world = event.getEntity().getWorld();

        if (event.getEntity() instanceof Player player) {
            if (event.getDamager() instanceof Player damager) {

                if (world.getName().equalsIgnoreCase("Spawn")) {
                    event.setCancelled(true);
                    damager.sendMessage(SkyMythPlugin.PREFIX + "§cDazu hast du keine Rechte.");
                    return;
                }


                if (Util.FREEZE.contains(player) || Util.FREEZE.contains(damager)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
