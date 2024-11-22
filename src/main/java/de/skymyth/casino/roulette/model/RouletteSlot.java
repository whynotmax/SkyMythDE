package de.skymyth.casino.roulette.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RouletteSlot {

    BLACK(2),
    RED(2),
    GREEN(10);

    long times;
}
