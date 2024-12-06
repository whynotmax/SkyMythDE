package de.skymyth.casino.roulette.model.slot;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RouletteSlot {

    BLACK("§8§lSchwarz", (short) 15, 2),
    RED("§c§lRot", (short) 14, 2),
    GREEN("§a§lGrün", (short) 5, 5);

    String displayName;
    short  data;

    long multiplier;
}
