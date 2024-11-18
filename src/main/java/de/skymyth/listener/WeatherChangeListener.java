package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public record WeatherChangeListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(event.getWorld().getName().equalsIgnoreCase("Spawn") || event.getWorld().getName().equalsIgnoreCase("PvP"));
    }

}
