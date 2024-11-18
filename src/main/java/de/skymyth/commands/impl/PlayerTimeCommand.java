package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerTimeCommand extends MythCommand {

    public PlayerTimeCommand(SkyMythPlugin plugin) {
        super("ptime", "myth.playertime", List.of("playertime"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /ptime <zeit/reset>");
            return;
        }
        if (args[0].equalsIgnoreCase("reset")) {
            player.resetPlayerTime();
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Die Zeit wurde zurückgesetzt.");
            return;
        }
        try {
            long time = Long.parseLong(args[0]);
            player.setPlayerTime(time, false);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Die Zeit wurde auf §e" + time + " §7gesetzt.");
        } catch (NumberFormatException e) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cUngültige Zeit.");
        }
    }
}
