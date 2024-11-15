package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BroadcastCommand extends MythCommand {

    public BroadcastCommand(SkyMythPlugin plugin) {
        super("broadcast", "myth.broadcastz", new ArrayList<>() {{
            add("bc");
        }}, plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length > 0) {
            StringBuilder message = new StringBuilder();

            for (String arg : args) {
                message.append(arg).append(" ");
            }

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage("§r");
                onlinePlayer.sendMessage(SkyMythPlugin.PREFIX + message.toString().replaceAll("&", "§"));
                onlinePlayer.sendMessage("§r");
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);
            }

            return;
        }
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /broadcast <nachricht>");
    }
}
