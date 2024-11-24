package de.skymyth.giveaway.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.giveaway.model.Giveaway;
import de.skymyth.giveaway.title.RandomPlayerScrambleTitle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class RandomPlayerGiveaway extends Giveaway {

    boolean finished;

    public RandomPlayerGiveaway(SkyMythPlugin plugin) {
        super(plugin);
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) Bukkit.broadcastMessage("§r");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Es wurde ein Giveaway gestartet!");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Ein Gewinner wird §ejetzt §7ermittelt.");
        Bukkit.broadcastMessage("§r");

        Player winner = determineWinner();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            new RandomPlayerScrambleTitle(plugin).showScrambleTitle(winner.getName(), player -> {
                Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Der Gewinner ist §e" + player.getName() + "§7!");
                plugin.getScoreboardManager().updateScoreboard(player);
                finished = true;
            });
        }, 20L);
    }

    @Override
    public Player determineWinner() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        List<? extends Player> players = List.copyOf(onlinePlayers);
        players = players.stream().filter(p -> !p.hasPermission("skymyth.team")).toList();
        return players.get((int) (Math.random() * players.size()));
    }

    @Override
    public boolean done() {
        return false;
    }
}
