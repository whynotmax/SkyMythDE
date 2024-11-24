package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.user.model.User;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LastSeenCommand extends MythCommand {

    public LastSeenCommand(SkyMythPlugin plugin) {
        super("lastseen", "myth.lastseen", new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        if (args.length == 0) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: §7/lastseen <Spieler>");
            return;
        }

        String playerName = args[0];
        User user = plugin.getUserManager().getUser(UUIDFetcher.getUUID(playerName));

        if (user == null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler wurde nicht gefunden.");
            return;
        }

        Player target = Bukkit.getPlayer(playerName);

        if (target != null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + playerName + " §7ist im Moment online!");
            return;
        }

        player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + playerName + " §7war zuletzt vor §e" +
                TimeUtil.beautifyTime(System.currentTimeMillis() - user.getLastSeen(), TimeUnit.MILLISECONDS, true, true) + " Online.");

        return;
    }
}

