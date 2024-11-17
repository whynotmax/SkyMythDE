package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.ui.EnderChestInventory;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class EnderChestCommand extends MythCommand {

    public EnderChestCommand(SkyMythPlugin plugin) {
        super("enderchest", null, List.of("lager", "ec"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            plugin.getInventoryManager().openInventory(player, new EnderChestInventory(plugin.getUserManager().getUser(player.getUniqueId()), player, plugin));
            return;
        }
        String target = args[0];
        OfflinePlayer targetPlayer = plugin.getServer().getOfflinePlayer(target);
        if (targetPlayer == null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler existiert nicht.");
            return;
        }
        plugin.getInventoryManager().openInventory(player, new EnderChestInventory(plugin.getUserManager().getUser(targetPlayer.getUniqueId()), player, plugin));
    }
}
