package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public record WeatherChangeListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        World world = event.getWorld();

        if (world.getName().equalsIgnoreCase("Spawn")) {
            world.setWeatherDuration(0);
            world.setThundering(false);
            world.setTime(0);
        }
    }
}
