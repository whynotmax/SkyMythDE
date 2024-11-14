package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.tpa.TeleportManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TPAHereCommand extends MythCommand {

    public TPAHereCommand(SkyMythPlugin plugin) {
        super("tpahere", null, new ArrayList<>() {{
            add("teleportrequesthere");
            add("tprequesthere");
            add("tpah");
        }}, plugin);
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

            if (TeleportManager.hasRequest(player.getUniqueId(), target.getUniqueId())) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu hast bereits eine Anfrage an diesen Spieler gesendet.");
                return;
            }

            TeleportManager.sendRequest(player.getUniqueId(), target.getUniqueId(), true);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast eine Teleportanfrage an §e" + target.getName() + " §7gesendet.");
            target.sendMessage(SkyMythPlugin.PREFIX + "§e" + player.getName() + " §7möchte, dass du sich zu ihm teleportierst. §7§o(/tpaccept)");
        } else {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cBenutze: §7/tpahere <Spieler>");
        }
    }
}
