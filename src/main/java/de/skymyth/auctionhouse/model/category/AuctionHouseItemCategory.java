package de.skymyth.auctionhouse.model.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public enum AuctionHouseItemCategory {

    PVP("PvP", Material.DIAMOND_SWORD),
    TOOLS("Werkzeuge", Material.DIAMOND_PICKAXE),
    FOOD("Essen", Material.COOKED_BEEF),
    BLOCKS("Blöcke", Material.DIRT),
    REDSTONE("Redstone", Material.REDSTONE),
    MISC("Sonstiges", Material.CHEST),
    SPECIAL("Spezial", Material.EMERALD);

    String name;
    Material displayItem;

}
