package de.skymyth.pvp.model;

import de.skymyth.utility.item.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public enum PvPShopItems {

    GOLDEN_APPLE(new ItemStack(Material.GOLDEN_APPLE), "§6Goldener PvP Apfel", 10),
    FISHING_ROD(new ItemStack(Material.FISHING_ROD), "§3PvP Angel", 15),
    SPEED_POTION(new ItemBuilder(Material.POTION)
            .setDataId(16386), "§bSpeed PvP Trank", 20),
    NOTCH_APPLE(new ItemBuilder(Material.GOLDEN_APPLE)
            .setDataId(1), "§e§lVerzauberter Goldener PvP Apfel", -1);

    ItemStack itemStack;
    String displayname;
    long price;

}
