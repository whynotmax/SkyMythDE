package de.skymyth.utility;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class NumberUtils {

    private static final Random RANDOM = new Random();

    public int randomInt(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

}
