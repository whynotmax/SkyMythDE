package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class GiveAllCommand extends MythCommand {

    public GiveAllCommand(SkyMythPlugin plugin) {
        super("giveall", "myth.giveall", List.of("givea"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /giveall <item> [anzahl]");
            return;
        }

        int amount = 1;
        if (args.length > 1) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Bitte gebe eine gültige Anzahl an.");
                return;
            }
        }

        ItemBuilder item = new ItemBuilder(Material.valueOf(args[0].toUpperCase()));
        item.amount(amount);
        for (Player onlinePlayer : player.getWorld().getPlayers()) {
            onlinePlayer.getInventory().addItem(item);
            onlinePlayer.updateInventory();
            onlinePlayer.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + amount + "x " + args[0] + " §7erhalten.");
        }

        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast jedem §e" + amount + "x " + args[0] + " §7gegeben.");
    }
}
