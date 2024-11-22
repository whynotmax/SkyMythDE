package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RankingCommand extends MythCommand {

    public RankingCommand(SkyMythPlugin plugin) {
        super("ranking", "myth.admin", new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        plugin.getRankingManager().update();
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Das Ranking wurde §esofort §7geupdated.");
    }
}
