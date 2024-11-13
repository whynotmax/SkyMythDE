package de.skymyth.giveaway.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.giveaway.model.Giveaway;
import de.skymyth.giveaway.title.RandomPlayerScrambleTitle;
import de.skymyth.user.model.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Locale;

public class TokenGiveaway extends Giveaway {

    long amount;
    boolean finished;

    public TokenGiveaway(SkyMythPlugin plugin, long amount) {
        super(plugin);
        this.amount = amount;
        this.finished = false;
    }

    @Override
    public void run() {
        //TODO: Disable chat
        for (int i = 0; i < 20; i++) Bukkit.broadcastMessage("§r");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Es wurde ein Token Giveaway gestartet!");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Zu gewinnen gibt es §e" + NumberFormat.getInstance(Locale.GERMAN).format(amount) + " Tokens§7.");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Ein Gewinner wird §ejetzt §7ermittelt.");
        Bukkit.broadcastMessage("§r");

        Player winner = determineWinner();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            new RandomPlayerScrambleTitle(plugin).showScrambleTitle(winner.getName(), player -> {
                Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Der Gewinner des Token Giveaways ist §e" + player.getName() + "§7!");
                User user = plugin.getUserManager().getUser(player.getUniqueId());
                user.addBalance(amount);
                plugin.getUserManager().saveUser(user);
                plugin.getScoreboardManager().updateScoreboard(player);
                finished = true;
            });
        }, 20L);
    }

    @Override
    public Player determineWinner() {
        return Bukkit.getOnlinePlayers().stream().findAny().orElse(null);
    }

    @Override
    public boolean done() {
        return finished;
    }
}
