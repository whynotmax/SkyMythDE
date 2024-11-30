package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.ui.SettingsInventory;
import org.bukkit.entity.Player;

import java.util.List;

public class SettingsCommand extends MythCommand {

    public SettingsCommand(SkyMythPlugin plugin) {
        super("settings", null, List.of("einstellungen", "setting"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        plugin.getInventoryManager().openInventory(player, new SettingsInventory(plugin, plugin.getUserManager().getUser(player.getUniqueId())));
    }
}
