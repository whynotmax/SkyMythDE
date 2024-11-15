package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.punish.model.result.PunishCheckResult;
import de.skymyth.punish.model.type.PunishType;
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
        event.allow();
    }

}
