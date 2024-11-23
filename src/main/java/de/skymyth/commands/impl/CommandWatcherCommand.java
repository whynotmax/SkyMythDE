package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.Util;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandWatcherCommand extends MythCommand {

    public CommandWatcherCommand(SkyMythPlugin plugin) {
        super("commandwatcher", "myth.cw", List.of("cw"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        if(Util.COMMANDWATCHER.contains(player)) {
            Util.COMMANDWATCHER.remove(player);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Command-Watcher wurde §cdeaktiviert.");
        } else {
            Util.COMMANDWATCHER.add(player);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Command-Watcher wurde §aaktiviert.");
        }
    }
}
