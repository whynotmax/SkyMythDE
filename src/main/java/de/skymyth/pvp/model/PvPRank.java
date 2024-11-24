package de.skymyth.pvp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PvPRank {

    UNRANKED(0, 249, "§7Ungewertet"),
    BRONZE(250, 499, "§cBronze"),
    SILVER(500, 749, "§7Silber"),
    GOLD(750, 999, "§6Gold"),
    PLATIN(1000, 1249, "§3Platin"),
    DIAMANT(1250, 1499, "§bDiamant"),
    SMARAGD(1500, 1749, "§aSmaragd"),
    RUBIN(1750, 1999, "§4Rubin"),
    SAPHIR(2000, 2249, "§2Saphir"),
    LEGENDE(2250, 2499, "§dLegende"),
    GOD(2500, Long.MAX_VALUE, "§6§lGott");

    long minTrophies;
    long maxTrophies;
    String displayName;

    public static PvPRank getRank(long trophies) {
        for (PvPRank rank : values()) {
            if (trophies >= rank.minTrophies && trophies <= rank.maxTrophies) {
                return rank;
            }
        }
        return UNRANKED;
    }
}

