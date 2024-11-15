package de.skymyth.rewards;

import java.util.Date;

public class RewardsManager {



    private String getCurrentSeason() {
        Date date = new Date();
        int month = date.getMonth();

        if (month <= 2) {
            return "Winter";
        } else if (month <= 5) {
            return "Frühling";
        } else if (month <= 8) {
            return "Sommer";
        } else {
            return "Herbst";
        }
    }

}
