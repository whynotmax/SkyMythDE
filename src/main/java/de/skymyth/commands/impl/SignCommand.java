package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SignCommand extends MythCommand {

    public SignCommand(SkyMythPlugin plugin) {
        super("sign", "myth.sign", List.of("signieren"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        String message = String.join(" ", args).replace("&", "§").replace("§§", "&");
        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst ein Item in der Hand halten.");
            return;
        }
        ItemBuilder item = new ItemBuilder(player.getInventory().getItemInHand().clone());
        item.addToLore(
                "§8§m----------------------§r",
                "§7Signiert von: §e" + player.getName()
        );
        if (!message.isEmpty()) {
            item.addToLore(
                    "§7Nachricht: §e" + message
            );
        }
        item.addToLore(
                "§8§m----------------------§r",
                "§7§oSigniertes Item"
        );
        player.getInventory().setItemInHand(item);
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Das Item wurde signiert.");
    }
}
