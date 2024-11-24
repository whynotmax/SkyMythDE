package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.casino.dailypot.ui.DailyPotMainInventory;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class DailyPotCommand extends MythCommand {

    public DailyPotCommand(SkyMythPlugin plugin) {
        super("dailypot", null, List.of("dp"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        plugin.getInventoryManager().openInventory(player, new DailyPotMainInventory(plugin));

    }
}
