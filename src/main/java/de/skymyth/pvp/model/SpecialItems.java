package de.skymyth.pvp.model;

import de.skymyth.utility.item.ItemBuilder;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum SpecialItems {

    FLAME_ROSE("Brennende Rose", "§cBrennende §6Rose", new ItemBuilder(Material.RED_ROSE)
            .setName("§cBrennende §6Rose"), 1000, true);

    String name;
    String displayName;
    ItemStack itemStack;
    long price;
    boolean enabled;

    SpecialItems(String name, String displayName, ItemStack itemStack, long price, boolean enabled) {
        this.name = name;
        this.displayName = displayName;
        this.itemStack = itemStack;
        this.price = price;
        this.enabled = enabled;
    }
}
