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

public class BanCommand extends MythCommand {

    public BanCommand(SkyMythPlugin plugin) {
        super("ban", "myth.ban", List.of("bann"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /ban <Spieler> <ID>");
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Folgende ID's stehen zur Verfügung:");
            int index = 1;
            for (PunishReason reason : PunishReason.getReasonsByType(PunishType.BAN)) {
                player.sendMessage("§8- #§e" + index++ + " §7" + reason.getName() + " §8(§7" + reason.getDescription() + "§8) [§c" + TimeUtil.beautifyTime(reason.getPunishDuration().toMillis(), TimeUnit.MILLISECONDS, true, true) + "§8]");
            }
            return;
        }
        String targetName = args[0];
        int id;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDie ID muss eine Zahl sein.");
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Folgende ID's stehen zur Verfügung:");
            int index = 1;
            for (PunishReason reason : PunishReason.getReasonsByType(PunishType.BAN)) {
                player.sendMessage("§8- #§e" + index++ + " §7" + reason.getName() + " §8(§7" + reason.getDescription() + "§8) [§c" + TimeUtil.beautifyTime(reason.getPunishDuration().toMillis(), TimeUnit.MILLISECONDS, true, true) + "§8]");
            }
            return;
        }
        List<PunishReason> banReasons = PunishReason.getReasonsByType(PunishType.BAN);
        if (id < 1 || id > banReasons.size()) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Die ID muss zwischen 1 und " + banReasons.size() + " liegen.");
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Folgende ID's stehen zur Verfügung:");
            int index = 1;
            for (PunishReason reason : PunishReason.getReasonsByType(PunishType.BAN)) {
                player.sendMessage("§8- #§e" + index++ + " §7" + reason.getName() + " §8(§7" + reason.getDescription() + "§8) [§c" + TimeUtil.beautifyTime(reason.getPunishDuration().toMillis(), TimeUnit.MILLISECONDS, true, true) + "§8]");
            }
            return;
        }
        PunishReason reason = banReasons.get(id - 1);
        UUID targetUUID = UUIDFetcher.getUUID(targetName);
        plugin.getPunishManager().ban(targetUUID, reason);
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + targetName + " §7wurde für §c" + TimeUtil.beautifyTime(reason.getPunishDuration().toMillis(), TimeUnit.MILLISECONDS, true, true) + " §7gebannt.");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Grund: §e" + reason.getDescription());
    }
}
