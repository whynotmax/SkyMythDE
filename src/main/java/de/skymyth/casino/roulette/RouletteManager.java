package de.skymyth.casino.roulette;

import de.skymyth.SkyMythPlugin;
import de.skymyth.casino.roulette.model.RouletteGame;
import de.skymyth.utility.Util;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouletteManager {

    SkyMythPlugin plugin;
    Map<Player, RouletteGame> rouletteGameMap;
    Map<Integer, Block> rouletteBlocks;
    BukkitTask bukkitTask;

    AtomicInteger secondsToStart = new AtomicInteger(30);
    AtomicInteger ticksToRoll = new AtomicInteger(1);
    Random random = Util.RANDOM;
    boolean aBlockIsGreen;

    boolean running;

    public RouletteManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.rouletteGameMap = new HashMap<>();
        this.rouletteBlocks = new HashMap<>();

        rouletteBlocks.put(1, plugin.getLocationManager().getPosition("roulette-1").getLocation().getBlock());
        rouletteBlocks.put(2, plugin.getLocationManager().getPosition("roulette-2").getLocation().getBlock());
        rouletteBlocks.put(3, plugin.getLocationManager().getPosition("roulette-3").getLocation().getBlock());
        rouletteBlocks.put(4, plugin.getLocationManager().getPosition("roulette-4").getLocation().getBlock());
        rouletteBlocks.put(5, plugin.getLocationManager().getPosition("roulette-5").getLocation().getBlock());

    }

    public void startGame(Player player, RouletteGame rouletteGame) {
        this.rouletteGameMap.put(player, rouletteGame);

        AtomicInteger ticks = new AtomicInteger();

        //

        if (bukkitTask != null && running) {
            //Roulette läuft bereits
            return;
        }

        this.bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            ticks.getAndIncrement();
            if (ticks.get() % 20 != secondsToStart.get()) {
                secondsToStart.getAndDecrement();
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Das Roulette startet in §e" + secondsToStart.get() + " §7Sekunden.");
                return;
            }
            if (secondsToStart.get() == 0) {
                running = true;
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Das Roulette startet jetzt.");
                ticksToRoll.getAndIncrement();
                secondsToStart.set(30);

                for (int i = 5; i > 1; i--) {

                }

                if (ticksToRoll.get() == 200) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Das Roulette ist vorbei.");
                    this.rouletteGameMap.remove(player);

                    Block block = rouletteBlocks.get(3);
                    Material material = block.getType();
                    if (block.getData() == 14 && material == Material.STAINED_CLAY) {
                        //Check dass der spieler den richtigen Block getroffen hat
                        //Rot
                    } else if (block.getData() == 15 && material == Material.STAINED_CLAY) {
                        //Check dass der spieler den richtigen Block getroffen hat
                        //Schwarz
                    } else {
                        //Check dass der spieler den richtigen Block getroffen hat
                        //Grün
                    }
                    Bukkit.getScheduler().cancelTask(bukkitTask.getTaskId());
                    running = false;
                    bukkitTask = null;
                    return;
                }
            }
        }, 1L, 1L);

    }

}
