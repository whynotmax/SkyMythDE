package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.badge.ui.BadgeInventory;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class BadgeCommand extends MythCommand {

    public BadgeCommand(SkyMythPlugin plugin) {
        super("badge", null, List.of("badges"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        plugin.getInventoryManager().openInventory(player, new BadgeInventory(plugin, player.getUniqueId()));
    }
}
