package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
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

        if (world.getName().equals("PvP") || world.getName().equals("FpsArena")) {
            if (event.getEntity() instanceof Player player) {
                if (world.getName().equals("FpsArena")) {
                    event.setCancelled(player.getLocation().distance(plugin.getLocationManager().getPosition("FpsArena").toBukkitLocation()) <= 7);
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDazu hast du keine Rechte.");
                }

                if(world.getName().equals("FpsArena") && player.getLocation().distance(plugin.getLocationManager().getPosition("FpsArena").toBukkitLocation()) <= 7) {
                    return;
                }

                if (event.getDamager() instanceof Player damager) {
                    if (player != damager) {
                        plugin.getCombatListener().startCombat(player, damager);
                    }
                } else if (event.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player projectShooter) {
                    if (projectShooter != player) {
                        plugin.getCombatListener().startCombat(player, projectShooter);
                    }
                }
            }
        }
    }
}
