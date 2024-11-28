package de.skymyth.casino;

import de.skymyth.SkyMythPlugin;
import de.skymyth.casino.dailypot.DailyPotManager;
import lombok.Getter;

@Getter
public class CasinoManager {

    SkyMythPlugin plugin;

    DailyPotManager dailyPotManager;

    public CasinoManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        // Deaktiviert weil "Es gibt keine Teilnehmer" die ganze Zeit im chat spammt???
        //this.dailyPotManager = new DailyPotManager(plugin);
    }

}
