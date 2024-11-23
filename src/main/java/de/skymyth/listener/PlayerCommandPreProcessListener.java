package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpMap;

public record PlayerCommandPreProcessListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();

        if(!command.contains("/msg")
                && !command.contains("/r")
                && !command.contains("/reply")) {

            for (Player player : Util.COMMANDWATCHER) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§e" + event.getPlayer().getName() + " §8➟ §8(§f" + command + "§8)");
            }

        }
    }
}
