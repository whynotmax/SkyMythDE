package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.UUIDFetcher;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PvPUnbanCommand extends MythCommand {

    public PvPUnbanCommand(SkyMythPlugin plugin) {
        super("pvpunban", "myth.pvpunban", List.of("pvpunpunish"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwendung: /pvpunban <Spieler>");
            return;
        }
        UUID target = UUIDFetcher.getUUID(args[0]);
        if (target == null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler existiert nicht.");
            return;
        }
        if (!plugin.getPvPPunishManager().isPunished(target)) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler ist nicht gebannt.");
            return;
        }
        plugin.getPvPPunishManager().unpunish(target);
        player.sendMessage(SkyMythPlugin.PREFIX + "§e" + UUIDFetcher.getName(target) + " §7wurde entbannt.");
    }
}
