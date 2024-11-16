package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.perks.ui.PerkInventory;
import org.bukkit.entity.Player;

import java.util.List;

public class PerkCommand extends MythCommand {

    public PerkCommand(SkyMythPlugin plugin) {
        super("perks", null, List.of("perk"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        plugin.getInventoryManager().openInventory(player, new PerkInventory(plugin, plugin.getUserManager().getUser(player.getUniqueId())));
    }
}
