package de.skymyth.rewards;

import de.skymyth.SkyMythPlugin;
import de.skymyth.location.model.Position;
import de.skymyth.utility.TimeUtil;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RewardsManager {

    SkyMythPlugin plugin;
    Position hologramPosition;
    Hologram hologram;
    BukkitTask hologramTask;

    boolean isInUse;

    long lastOpened;

    public RewardsManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.hologramPosition = plugin.getLocationManager().getPosition("rewards-hologram");

        this.hologram = DHAPI.createHologram("rewards-hologram", this.hologramPosition.toBukkitLocation().add(0, 1, 0), false, List.of(
                "§f§lW§c§li§f§ln§c§lt§f§le§c§lr §f§lR§c§le§f§lw§c§la§f§lr§c§ld§f§ls",
                "§r",
                "§7Aktuelle Saison: §e" + "Winter",
                "§7Letzte Öffnung: §cUnbekannt",
                "§r",
                "§7§oKlicke auf den Block unten,",
                "§7§o um deine Belohnung abzuholen."
        ));

        this.hologramTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (isInUse) return;
            if (lastOpened == 0) {
                DHAPI.setHologramLine(this.hologram, 3, "§7Letzte Öffnung: §cUnbekannt");
                return;
            }
            DHAPI.setHologramLine(this.hologram, 3, "§7Letzte Öffnung: §e" + TimeUtil.beautifyTime(System.currentTimeMillis() - lastOpened, TimeUnit.MILLISECONDS, true, false));
        }, 0L, 20*5L);

    }

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
