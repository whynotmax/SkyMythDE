package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class SpawnCommand extends MythCommand {

    private final Map<Player, BukkitRunnable> teleportTasks = new HashMap<>();

    public SpawnCommand(SkyMythPlugin plugin) {
        super("spawn", "", new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (teleportTasks.containsKey(player)) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu wurdest bereits zum Spawn teleportiert.");
            return;
        }

        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du wirst in 3 Sekunden zum Spawn teleportiert.");

        Location initialLocation = player.getLocation();

        BukkitRunnable task = new BukkitRunnable() {
            private int ticks = 0;

            @Override
            public void run() {
                if (!player.getLocation().getBlock().equals(initialLocation.getBlock())) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Vorgang wurde abgebrochen, da du dich bewegt hast.");
                    teleportTasks.remove(player);
                    this.cancel();
                    return;
                }

                ticks++;

                if (ticks >= 60) {
                    teleportTasks.remove(player);
                    player.teleport(plugin.getLocationManager().getPosition("spawn").getLocation());
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Du wurdest zum §eSpawn §7teleportiert.");
                    this.cancel();
                }
            }
        };

        task.runTaskTimer(plugin, 0L, 1L);
        teleportTasks.put(player, task);
    }
}
