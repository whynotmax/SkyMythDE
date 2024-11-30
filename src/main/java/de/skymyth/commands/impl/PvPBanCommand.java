package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.UUIDFetcher;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PvPBanCommand extends MythCommand {

    public PvPBanCommand(SkyMythPlugin plugin) {
        super("pvpban", "myth.pvpban", List.of("pvppunish"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(SkyMythPlugin.PREFIX + "Verwendung: /pvpban <Spieler> <Grund> [Dauer (optional, z.B. 1d)]");
            return;
        }
        UUID target = UUIDFetcher.getUUID(args[0]);
        if (target == null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler ist nicht online.");
            return;
        }
        if (plugin.getPvPPunishManager().isPunished(target)) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler ist bereits gebannt.");
            return;
        }
        long duration = -1;
        if (args.length == 3) {
            try {
                if (!args[2].equalsIgnoreCase("permanent") && !args[2].equalsIgnoreCase("perm") && !args[2].equalsIgnoreCase("-1")) {
                    duration = TimeUtil.parseTimeFromString(args[2]);
                }
            } catch (NumberFormatException e) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cUngültige Dauer.");
                return;
            }
        }
        plugin.getPvPPunishManager().punish(target, args[1], duration);
    }
}
