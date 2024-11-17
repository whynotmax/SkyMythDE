package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public record BlockBreakListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!player.isOp()) {
            if (world.getName().equalsIgnoreCase("Spawn") || world.getName().equalsIgnoreCase("PvP")) {
                event.setCancelled(true);
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDazu hast du keine Rechte.");
            }
        }
    }

}
