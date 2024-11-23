package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.maintenance.model.Maintenance;
import de.skymyth.punish.model.result.PunishCheckResult;
import de.skymyth.punish.model.type.PunishType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public record PlayerLoginListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onLogin(final PlayerLoginEvent event) {
        Player player = event.getPlayer();
        PunishCheckResult result = plugin.getPunishManager().check(player.getUniqueId());
        if (result.isPunished() && result.getPunish().getType() == PunishType.BAN && result.getPunish().getRemaining() > 0) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, plugin.getPunishManager().getBanScreen(result.getPunish()));
            return;
        }

        if (plugin.getMaintenanceManager().getMaintenance().isEnabled() && !plugin.getMaintenanceManager().isWhitelisted(player.getUniqueId())) {
            Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§e" + player.getName() + " §7wollte den Server betreten.");
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST,
                    "\n" +
                            SkyMythPlugin.PREFIX + "§7Wartungsarbeiten\n" +
                            "\n" +
                            "§7Der Server ist momentan im Wartungsmodus.\n" +
                            "§7Unser Release ist am §e01. Dezember 2024§7.\n" +
                            "§7Bitte versuche es später erneut.\n" +
                            "\n" +
                            "§7Mehr Informationen auf unserem §bDiscord§7:\n" +
                            "§3§ndiscord.skymyth.de§r\n" +
                            "\n" +
                            SkyMythPlugin.PREFIX + "§7Wartungsarbeiten"
                    );
            return;
        }

        event.allow();
    }

}
