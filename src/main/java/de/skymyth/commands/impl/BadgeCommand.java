package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.badge.model.Badge;
import de.skymyth.badge.ui.BadgeInfoInventory;
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
        if (args.length == 2 && args[0].equalsIgnoreCase("info")) {
            Badge badge = plugin.getBadgeManager().getBadge(args[1]);
            if (badge == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieses Badge existiert nicht.");
                return;
            }
            plugin.getInventoryManager().openInventory(player, new BadgeInfoInventory(plugin, player, badge));
            return;
        }
        plugin.getInventoryManager().openInventory(player, new BadgeInventory(plugin, player.getUniqueId()));
    }
}
