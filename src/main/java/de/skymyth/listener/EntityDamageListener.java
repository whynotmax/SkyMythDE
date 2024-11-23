package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.Util;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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

        if (world.getName().equals("Spawn")) {
            event.setCancelled(true);
        }

        if (world.getName().equals("world")) {
            if (event.getEntity() instanceof Player player) {
                if (event.getDamager() instanceof Player damager || event.getDamager() instanceof Projectile projectile) {
                    event.setCancelled(true);
                }
            }
        }

        if (world.getName().equals("PvP")) {
            if (event.getEntity() instanceof Player player) {
                if (event.getDamager() instanceof Player damager) {
                    plugin.getCombatListener().startCombat(player, damager);

                } else if (event.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player projectShooter) {
                    plugin.getCombatListener().startCombat(player, projectShooter);
                }
            }
        }
    }
}
