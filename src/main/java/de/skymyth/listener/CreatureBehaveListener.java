package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public record CreatureBehaveListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        double distanceToSpawn = event.getEntity().getLocation().distance(plugin.getLocationManager().getPosition("Farmwelt").getLocation());

        if(Math.round(distanceToSpawn) < 500) {
            event.setCancelled(true);
        }
    }

}
