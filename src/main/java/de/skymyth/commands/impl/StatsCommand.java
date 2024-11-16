package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.ui.PlayerStatsInventory;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class StatsCommand extends MythCommand {

    public StatsCommand(SkyMythPlugin plugin) {
        super("stats", null, List.of("statistik", "statistiken", "statistic", "statistics"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            plugin.getInventoryManager().openInventory(player, new PlayerStatsInventory(plugin, player.getUniqueId()));
        } else {
            String target = args[0];
            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(target);
            if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                plugin.getInventoryManager().openInventory(player, new PlayerStatsInventory(plugin, offlinePlayer.getUniqueId()));
            } else {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler wurde nicht gefunden.");
            }
        }
    }
}
