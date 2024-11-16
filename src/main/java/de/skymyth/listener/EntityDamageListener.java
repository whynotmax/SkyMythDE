package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public record EntityDamageListener(SkyMythPlugin plugin) implements Listener {


    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(event.getDamager() instanceof Player damager) {

                if(Util.FREEZE.contains(player) || Util.FREEZE.contains(damager)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
