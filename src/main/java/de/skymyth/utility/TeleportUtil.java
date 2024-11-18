package de.skymyth.utility;

import de.skymyth.SkyMythPlugin;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class TeleportUtil {

    private static final Map<Player, BukkitRunnable> TELEPORT_TASKS = new HashMap<>();

    public static void createTeleportation(SkyMythPlugin plugin, Player player, Location location, String locationName) {
        Location initialLocation = player.getLocation();

        if(TELEPORT_TASKS.containsKey(player)) return;

        if (!player.hasPermission("myth.team")) {


            BukkitRunnable task = new BukkitRunnable() {
                private int ticks = 0;

                @Override
                public void run() {
                    if (!player.getLocation().getBlock().equals(initialLocation.getBlock())) {
                        player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Vorgang wurde abgebrochen, da du dich bewegt hast.");
                        TELEPORT_TASKS.remove(player);
                        this.cancel();
                        return;
                    }

                    int secondsLeft = 3 - (ticks / 20);

                    if (ticks % 20 == 0) {
                        player.playSound(player.getLocation(), Sound.LAVA_POP, 1.0f, 1.0f);
                        TitleUtil.sendTitle(player, 0, 40, 20, "§e§l" + secondsLeft, "§8× §7Du wirst teleportiert §8×");

                    }

                    ticks++;

                    if (ticks >= 60) {
                        TELEPORT_TASKS.remove(player);
                        player.teleport(location);
                        TitleUtil.sendTitle(player, 0, 40, 20, "§a§l" + locationName, "§8× §7Du wurdest teleportiert §8×");
                        player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0f, 1.0f);
                        this.cancel();
                    }
                }
            };

            task.runTaskTimer(plugin, 0L, 1L);
            TELEPORT_TASKS.put(player, task);
        } else {
            player.teleport(location);
            TitleUtil.sendTitle(player, 0, 40, 20, "§a§l" + locationName, "§8× §7Du wurdest teleportiert §8×");
            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0f, 1.0f);
        }
    }

}
