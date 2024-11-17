package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public record SoilChangeListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onSoilChange(BlockFromToEvent event) {
        World world = event.getBlock().getWorld();
        if (world.getName().equalsIgnoreCase("PvP")) {
            event.setCancelled(event.getBlock().getType() == Material.SOIL);
        }
    }

}
