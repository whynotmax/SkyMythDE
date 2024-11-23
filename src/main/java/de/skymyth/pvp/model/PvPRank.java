package de.skymyth.pvp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PvPRank {

    UNRANKED(0, 500, "Ungewertet"),
    BRONZE(501, 1000, "Bronze"),
    SILVER(1001, 1500, "Silber"),
    GOLD(1501, 2000, "Gold"),
    LEGEND(2001, Long.MAX_VALUE, "Meister");

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

