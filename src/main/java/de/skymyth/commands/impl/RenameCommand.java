package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class RenameCommand extends MythCommand {

    public RenameCommand(SkyMythPlugin plugin) {
        super("rename", "myth.rename", new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst ein Item in der Hand halten.");
            return;
        }

        if (args.length >= 1) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String arg : args) {
                stringBuilder.append(arg).append(" ");
            }

            ItemMeta meta = item.getItemMeta();
            if (meta == null) {
                meta = Bukkit.getItemFactory().getItemMeta(item.getType());
            }

            if (meta != null) {
                meta.setDisplayName(stringBuilder.toString().replaceAll("&", "§").trim());
                item.setItemMeta(meta);
                player.sendMessage(SkyMythPlugin.PREFIX + "§eDas Item wurde umbenannt.");
                player.updateInventory();
            } else {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDas Item unterstützt keine Meta.");
            }
            return;
        }

        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /rename <text>");
    }

}
