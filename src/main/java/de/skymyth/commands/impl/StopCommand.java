package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class StopCommand extends MythCommand {

    public StopCommand(SkyMythPlugin plugin) {
        super("stop", "bukkit.command.stop", List.of("restart"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Server wird gestoppt.");

        plugin.onDisable();

        plugin.getServer().shutdown();
    }
}
