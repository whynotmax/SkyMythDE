package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GlobalmuteCommand extends MythCommand {

    public GlobalmuteCommand(SkyMythPlugin plugin) {
        super("globalmute", "myth.globalmute", new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Status: " + (Util.GLOBALMUTE.get() ? "§aaktiviert" : "§cdeaktiviert"));
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /globalmute <on/off>");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "on":
                Util.GLOBALMUTE.set(true);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Globalmute §aaktiviert§7.");
                break;
            case "off":
                Util.GLOBALMUTE.set(false);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Globalmute §cdeaktiviert§7.");
                break;
            default:
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /globalmute <on/off>");
                break;
        }
    }
}
