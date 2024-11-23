package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.pvp.ui.liga.LigaMainInventory;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LigaCommand extends MythCommand {

    public LigaCommand(SkyMythPlugin plugin) {
        super("liga", null, new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        plugin.getInventoryManager().openInventory(player, new LigaMainInventory());
    }
}
