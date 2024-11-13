package de.skymyth.giveaway.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.giveaway.model.Giveaway;
import de.skymyth.giveaway.title.RandomPlayerScrambleTitle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class ItemGiveaway extends Giveaway {

    ItemStack itemStack;
    boolean finished;

    public ItemGiveaway(SkyMythPlugin plugin, ItemStack itemStack) {
        super(plugin);
        this.itemStack = itemStack.clone();
        this.finished = false;
    }

    @Override
    public void run() {
        //TODO: Disable chat
        for (int i = 0; i < 20; i++) Bukkit.broadcastMessage("§r");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Es wurde ein Item Giveaway gestartet!");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Zu gewinnen gibt es §6" + NumberFormat.getInstance(Locale.GERMAN).format(itemStack.getAmount()) + "§8x '§r" + (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : "§6" + itemStack.getType()) + "§8'§7.");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Ein Gewinner wird §ejetzt §7ermittelt.");
        Bukkit.broadcastMessage("§r");

        Player winner = determineWinner();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            new RandomPlayerScrambleTitle(plugin).showScrambleTitle(winner.getName(), player -> {
                Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Der Gewinner des Token Giveaways ist §e" + player.getName() + "§7!");
                player.getInventory().addItem(itemStack.clone());
                plugin.getScoreboardManager().updateScoreboard(player);
                finished = true;
            });
        }, 20L);
    }

    @Override
    public Player determineWinner() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        List<? extends Player> players = List.copyOf(onlinePlayers);
        return players.get((int) (Math.random() * players.size()));
    }

    @Override
    public boolean done() {
        return finished;
    }
}
