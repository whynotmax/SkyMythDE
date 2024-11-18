package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;

public class WorkbenchCommand extends MythCommand {

    public WorkbenchCommand(SkyMythPlugin plugin) {
        super("workbench", "myth.workbench", List.of("wb", "werkbank"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        player.openInventory(Bukkit.createInventory(null, InventoryType.WORKBENCH));
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast die Werkbank geöffnet.");
    }
}
