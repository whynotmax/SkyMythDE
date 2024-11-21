package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public record ServerPingListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        event.setMotd(SkyMythPlugin.PREFIX + "§c1.12.2024");
    }
}
