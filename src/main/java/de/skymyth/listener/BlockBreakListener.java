package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.protector.ProtectionManager;
import de.skymyth.protector.model.Protector;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public record BlockBreakListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ProtectionManager protectionManager = plugin.getProtectorManager();
        Protector userProtector = protectionManager.getProtector(event.getPlayer().getUniqueId());
        if (!event.getBlock().getWorld().getName().equalsIgnoreCase("world")) return;
        Chunk chunk = event.getBlock().getChunk();
        if (protectionManager.isProtected(chunk) && userProtector.getProtectedChunks().stream().noneMatch(baseChunk -> baseChunk.getX() == chunk.getX() && baseChunk.getZ() == chunk.getZ())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(SkyMythPlugin.PREFIX + "§cDieses Chunk ist von " + plugin.getServer().getOfflinePlayer(protectionManager.getProtector(chunk).getOwner()).getName() + " geschützt.");
        }
    }

}
