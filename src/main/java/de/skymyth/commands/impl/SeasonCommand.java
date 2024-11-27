package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SeasonCommand extends MythCommand {

    public SeasonCommand(SkyMythPlugin plugin) {
        super("Season", null, new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Die erste SkyMyth Season endet am §e31. Dezember 2024!");
    }
}
