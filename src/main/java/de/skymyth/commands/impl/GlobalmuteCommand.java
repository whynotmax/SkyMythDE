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

        if (Util.isGLOBALMUTE()) {
            Util.setGLOBALMUTE(false);
            Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Der Globalmute wurde §cdeaktiviert.");
        } else {
            Util.setGLOBALMUTE(true);
            Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Der Globalmute wurde §aaktiviert.");
        }
    }
}
