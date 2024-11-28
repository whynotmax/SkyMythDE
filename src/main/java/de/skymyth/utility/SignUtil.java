package de.skymyth.utility;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class SignUtil {

    public boolean isSigned(ItemStack itemStack) {
        return itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() && itemStack.getItemMeta().getLore().getLast().contains("§7§oSigniertes Item");
    }

    public ItemStack removeSignature(ItemStack itemStack) {
        if (!isSigned(itemStack)) {
            return itemStack;
        }
        ItemStack clone = itemStack.clone();
        clone.getItemMeta().getLore().removeLast();
        clone.getItemMeta().getLore().removeLast();
        clone.getItemMeta().getLore().removeLast();
        clone.getItemMeta().getLore().removeLast();
        if (clone.getItemMeta().getLore().getLast().equals("§8§m----------------------§r")) {
            clone.getItemMeta().getLore().removeLast();
        }
        return clone;
    }

}
