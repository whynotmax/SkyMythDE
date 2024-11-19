package de.skymyth.runnables;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

import java.util.concurrent.atomic.AtomicInteger;

public class AntiLagRunnable implements Runnable {

    int seconds = 750;
    int neededSeconds = 750;

    @Override
    public void run() {
        seconds--;
        switch (seconds) {
            case 60:
            case 30:
            case 10:
                Util.CANDROPITEMS = false;
                if (!Bukkit.getOnlinePlayers().isEmpty())
                    Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Es werden alle §eItems §7auf dem Boden in §e" + seconds + " Sekunden §7entfernt.");
                break;
            case 5:
            case 4:
            case 3:
            case 2:
                if (!Bukkit.getOnlinePlayers().isEmpty())
                    Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Es werden alle §eItems §7auf dem Boden in §e" + seconds + " Sekunden §7entfernt.");
                break;
            case 1:
                if (!Bukkit.getOnlinePlayers().isEmpty())
                    Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Es werden alle §eItems §7auf dem Boden in §e" + seconds + " Sekunde §7entfernt.");
                break;
            case 0:
                seconds = neededSeconds;
                AtomicInteger itemsRemoved = new AtomicInteger();
                Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(entity -> {
                    if (entity.getType() != EntityType.DROPPED_ITEM) return;
                    itemsRemoved.getAndIncrement();
                    entity.remove();
                }));
                if (!Bukkit.getOnlinePlayers().isEmpty())
                    Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Es wurden §e" + itemsRemoved.get() + " Items §7vom Boden entfernt.");
                Util.CANDROPITEMS = true;
                break;
        }
    }
}
