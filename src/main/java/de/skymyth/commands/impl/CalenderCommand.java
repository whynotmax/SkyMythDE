package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.advent.ui.CalenderMainInventory;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.util.List;

public class CalenderCommand extends MythCommand {

    public CalenderCommand(SkyMythPlugin plugin) {
        super("Calender", null, List.of("adventskalender", "kalender", "advent", "winter", "weihnachten"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        plugin.getInventoryManager().openInventory(player, new CalenderMainInventory(plugin, player));

    }
}
