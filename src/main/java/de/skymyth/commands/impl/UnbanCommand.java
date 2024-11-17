package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.punish.model.reason.PunishReason;
import de.skymyth.punish.model.type.PunishType;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.UUIDFetcher;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UnbanCommand extends MythCommand {

    public UnbanCommand(SkyMythPlugin plugin) {
        super("unban", "myth.ban", List.of("unbann"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /unban <Spieler>");
            return;
        }
        String targetName = args[0];
        UUID targetUUID = UUIDFetcher.getUUID(targetName);
        plugin.getPunishManager().unban(targetUUID);
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + targetName + " §7wurde entbannt.");
    }
}
