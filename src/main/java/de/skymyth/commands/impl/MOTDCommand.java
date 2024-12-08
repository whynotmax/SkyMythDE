package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class MOTDCommand extends MythCommand {

    public MOTDCommand(SkyMythPlugin plugin) {
        super("motd", "myth.motd", List.of(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /motd <1/2> <text>");
            return;
        }
        int line;
        try {
            line = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /motd <1/2> <text>");
            return;
        }
        if (line < 1 || line > 2) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /motd <1/2> <text>");
            return;
        }
        plugin.getMotdManager().setMotdLine(line, String.join(" ", args).replaceFirst(line + " ", ""));
        player.sendMessage(SkyMythPlugin.PREFIX + "§7MOTD §8(§eZeile " + line + "§8) §7geändert.");
    }
}
