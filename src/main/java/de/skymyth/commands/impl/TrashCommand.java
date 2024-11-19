package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class TrashCommand extends MythCommand {

    public TrashCommand(SkyMythPlugin plugin) {
        super("trash", null, List.of("müll", "garbage", "mülleimer"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        player.openInventory(Bukkit.createInventory(null, 36, "Müll"));

    }
}
