package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.ui.EnchanterInventory;
import org.bukkit.entity.Player;

import java.util.List;

public class EnchanterCommand extends MythCommand {

    public EnchanterCommand(SkyMythPlugin plugin) {
        super("enchanter", null, List.of(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        plugin.getInventoryManager().openInventory(player, new EnchanterInventory(plugin));
    }
}
