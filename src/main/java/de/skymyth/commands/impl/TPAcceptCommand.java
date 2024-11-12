package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.tpa.TeleportManager;
import de.skymyth.tpa.model.TeleportRequest;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TPAcceptCommand extends MythCommand {

    public TPAcceptCommand(SkyMythPlugin plugin) {
        super("tpaccept", null, new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 1) {
            Player target = plugin.getServer().getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler wurde nicht gefunden.");
                return;
            }

            if (target == player) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst dir selber keine Teleportanfragen senden.");
                return;
            }

            if (!TeleportManager.hasRequest(target.getUniqueId(), player.getUniqueId())) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu hast keine Anfrage von diesem Spieler erhalten.");
                return;
            }

            TeleportRequest request = TeleportManager.getRequest(target.getUniqueId(), player.getUniqueId());
            if (request == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cEin Fehler ist aufgetreten.");
                return;
            }

            if (request.isExpired()) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDie Anfrage ist abgelaufen.");
                return;
            }

            boolean isHere = request.isHere();
            if (isHere) {
                target.teleport(player);
            } else {
                player.teleport(target);
            }

            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast die Teleportanfrage von §e" + target.getName() + " §7angenommen.");
            target.sendMessage(SkyMythPlugin.PREFIX + "§e" + player.getName() + " §7hat deine Teleportanfrage angenommen.");
            TeleportManager.removeRequest(target.getUniqueId(), player.getUniqueId());

        } else {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cBenutze: §7/tpaccept <Spieler>");
        }
    }
}
