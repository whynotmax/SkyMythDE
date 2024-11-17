package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.UUIDFetcher;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class UnmuteCommand extends MythCommand {

    public UnmuteCommand(SkyMythPlugin plugin) {
        super("unmute", "myth.ban", List.of(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /unmute <Spieler>");
        }
        String targetName = args[0];
        UUID targetUUID = UUIDFetcher.getUUID(targetName);
        plugin.getPunishManager().unban(targetUUID);
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + targetName + " §7wurde entbannt.");
    }
}
