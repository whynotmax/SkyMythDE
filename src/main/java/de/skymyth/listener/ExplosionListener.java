package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public record ExplosionListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {

        for (Block block : event.blockList()) {

            if(block.getWorld().getName().equals("world")) {
                double distanceToSpawn = block.getLocation().distance(plugin.getLocationManager().getPosition("Farmwelt").getLocation());

                if(Math.round(distanceToSpawn) < 500) {
                    event.setCancelled(true);
                }
            }

            if (plugin.getBaseProtectorManager().isBlockProtected(block)) {
                event.setCancelled(true);
            }
        }


    }
}
