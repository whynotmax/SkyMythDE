package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public record BlockPlaceListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
//        ProtectionManager protectionManager = plugin.getProtectorManager();
//        Protector userProtector = protectionManager.getProtector(event.getPlayer().getUniqueId());
//        if (!event.getBlockPlaced().getWorld().getName().equalsIgnoreCase("world")) return;
//        Chunk chunk = event.getBlockPlaced().getChunk();
//        if (protectionManager.isProtected(chunk) && userProtector.getProtectedChunks().stream().noneMatch(baseChunk -> baseChunk.getX() == chunk.getX() && baseChunk.getZ() == chunk.getZ())) {
//            event.setCancelled(true);
//            event.getPlayer().sendMessage(SkyMythPlugin.PREFIX + "§cDieses Chunk ist von " + plugin.getServer().getOfflinePlayer(protectionManager.getProtector(chunk).getOwner()).getName() + " geschützt.");
//            return;
//        }

    }

}
