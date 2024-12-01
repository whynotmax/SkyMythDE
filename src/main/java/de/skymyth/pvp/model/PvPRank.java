package de.skymyth.pvp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public enum PvPRank {

    UNRANKED(0, 249, "§7Ungewertet", Material.WOOD_SWORD),
    BRONZE(250, 499, "§cBronze", Material.STONE_SWORD),
    SILVER(500, 749, "§7Silber", Material.IRON_SWORD),
    GOLD(750, 999, "§6Gold", Material.GOLD_SWORD),
    PLATIN(1000, 1249, "§3Platin", Material.DIAMOND_SWORD),
    DIAMANT(1250, 1499, "§bDiamant", Material.DIAMOND),
    SMARAGD(1500, 1749, "§aSmaragd", Material.EMERALD),
    RUBIN(1750, 1999, "§4Rubin", Material.REDSTONE),
    SAPHIR(2000, 2249, "§2Saphir", Material.GREEN_RECORD),
    LEGENDE(2250, 2499, "§dLegende", Material.RECORD_3),
    GOD(2500, Long.MAX_VALUE, "§6§lGott", Material.NETHER_STAR);

    long minTrophies;
    long maxTrophies;
    String displayName;
    Material display;

    public static PvPRank getRank(long trophies) {
        for (PvPRank rank : values()) {
            if (trophies >= rank.minTrophies && trophies <= rank.maxTrophies) {
                return rank;
            }
        }
        return UNRANKED;
    }
}

