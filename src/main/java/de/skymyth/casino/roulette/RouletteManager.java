package de.skymyth.casino.roulette;

import de.skymyth.SkyMythPlugin;
import de.skymyth.casino.roulette.model.RouletteGame;
import jodd.util.concurrent.Task;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouletteManager {

    SkyMythPlugin plugin;
    Map<Player, RouletteGame> rouletteGameMap;
    Map<Integer, Block> rouletteBlocks;
    BukkitTask bukkitTask;

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

        AtomicInteger seconds = new AtomicInteger(30);
        this.rouletteGameMap.put(player, rouletteGame);

        bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {



            if(seconds.getAndDecrement() == 0) {
                bukkitTask.cancel();
                player.sendMessage("Ende");
            }


        }, 0L, 20);

    }
}
