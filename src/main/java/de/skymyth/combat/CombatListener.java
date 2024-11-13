package de.skymyth.combat;

import de.skymyth.SkyMythPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public record CombatListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player player && e.getDamager() instanceof Player target) {
            String world = player.getLocation().getWorld().getName();

            if (!world.equals("Spawn")) {
                if (LogoutListener.combat.add(player) && LogoutListener.combat.add(target)) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Du bist nun im Kampf§8.");
                    target.sendMessage(SkyMythPlugin.PREFIX + "§7Du bist nun im Kampf§8.");
                }
            } else {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cAm Spawn ist PvP deaktiviert§8.");
                e.setCancelled(true);
            }

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new BukkitRunnable() {
                @Override
                public void run() {
                    if (LogoutListener.combat.remove(player) && LogoutListener.combat.remove(target)) {
                        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du bist nun nicht mehr im Kampf§8.");
                        target.sendMessage(SkyMythPlugin.PREFIX + "§7Du bist nun nicht mehr im Kampf§8.");
                    }
                }
            }, 150L);
        }
    }
}
