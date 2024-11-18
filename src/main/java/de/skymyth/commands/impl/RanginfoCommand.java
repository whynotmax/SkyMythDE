package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.ui.RanginfoInventory;
import org.bukkit.entity.Player;

import java.util.List;

public class RanginfoCommand extends MythCommand {

    public RanginfoCommand(SkyMythPlugin plugin) {
        super("ranginfo", null, List.of("ranks"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        plugin.getInventoryManager().openInventory(player, new RanginfoInventory(player));
    }
}
