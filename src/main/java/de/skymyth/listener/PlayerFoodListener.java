package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public record PlayerFoodListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onFoodLevel(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

}
