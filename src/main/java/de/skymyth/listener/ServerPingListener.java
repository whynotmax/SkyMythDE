package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public record ServerPingListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        if (plugin.getMaintenanceManager().getMaintenance().isEnabled()) {
            event.setMotd(plugin.getMaintenanceManager().getMaintenance().getMotdLine1().replace('&', '§') + "\n" + plugin.getMaintenanceManager().getMaintenance().getMotdLine2().replace('&', '§'));
            event.setMaxPlayers(0);
            return;
        }
        event.setMotd(plugin.getMotdManager().getMotdLine1().replace('&', '§') + "\n" + plugin.getMotdManager().getMotdLine2().replace('&', '§'));
        event.setMaxPlayers(Bukkit.getOnlinePlayers().size() + 1);
    }
}
