package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HatCommand extends MythCommand {

    public HatCommand(SkyMythPlugin plugin) {
        super("hat", "myth.hat", List.of("hut"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        ItemStack currentHelmet = player.getInventory().getHelmet();
        ItemStack currentHand = player.getInventory().getItemInHand();

        if (currentHand == null || currentHand.getType() == Material.AIR) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst ein Item in der Hand halten.");
            return;
        }

        if (currentHelmet != null) {
            player.getInventory().setItemInHand(currentHelmet);
        } else {
            player.getInventory().setItemInHand(new ItemStack(Material.AIR));
        }
        player.getInventory().setHelmet(currentHand);
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast deinen Hut gewechselt.");
    }
}
