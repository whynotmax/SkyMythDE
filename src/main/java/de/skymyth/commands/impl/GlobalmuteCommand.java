package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GlobalmuteCommand extends MythCommand {

    public GlobalmuteCommand(SkyMythPlugin plugin) {
        super("globalmute", "myth.globalmute", new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        if (plugin.isGlobalMute()) {
            plugin.setGlobalMute(false);
            Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Der Globalmute wurde §cdeaktiviert.");
        } else {
            plugin.setGlobalMute(true);
            Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Der Globalmute wurde §aaktiviert.");
        }
    }
}
