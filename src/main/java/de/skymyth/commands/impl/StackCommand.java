package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StackCommand extends MythCommand {

    public StackCommand(SkyMythPlugin plugin) {
        super("stack", "myth.stack", List.of(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        ItemStack[] itemStacks = new ItemStack[player.getInventory().getSize()];
        ItemStack heldItem = player.getInventory().getItemInHand();

        if (heldItem == null || heldItem.getType() == Material.AIR) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst ein Item in der Hand halten.");
            return;
        }

        int i = 0;
        if (heldItem.getType() != Material.AIR) {
            for (ItemStack inventoryStack : player.getInventory().getContents()) {
                if (inventoryStack == null || inventoryStack.getType() == Material.AIR) {
                    continue;
                }
                if (inventoryStack.isSimilar(heldItem)) {
                    itemStacks[i] = inventoryStack;
                    i++;
                    break;
                }
            }
        }
        stack(player, itemStacks);
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Das Item wurde gestackt.");
    }

    private void stack(Player player, ItemStack... itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                continue;
            }
            for (ItemStack content : player.getInventory().getContents()) {
                if (content == null || content.getType() == Material.AIR) {
                    player.getInventory().addItem(itemStack);
                    break;
                }
                if (content.isSimilar(itemStack) && content.getAmount() < content.getMaxStackSize()) {
                    int amount = content.getAmount() + itemStack.getAmount();
                    if (amount > content.getMaxStackSize()) {
                        int rest = amount - content.getMaxStackSize();
                        content.setAmount(content.getMaxStackSize());
                        itemStack.setAmount(rest);
                    } else {
                        content.setAmount(amount);
                        break;
                    }
                }
            }
        }
        player.updateInventory();
    }
}
