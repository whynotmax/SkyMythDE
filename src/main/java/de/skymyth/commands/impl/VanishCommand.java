package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.user.model.User;
import de.skymyth.utility.Util;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class VanishCommand extends MythCommand {


    public VanishCommand(SkyMythPlugin plugin) {
        super("vanish", "myth.vanish", new ArrayList<>() {{
            add("v");
        }}, plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        if (!Util.VANISH.contains(player)) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.hasPermission("myth.team")) {
                    onlinePlayer.hidePlayer(player);
                }
            }

            player.sendMessage(SkyMythPlugin.PREFIX + "§aDu bist nun im Vanish, niemand außer Teammitglieder sehen dich.");
            player.playSound(player.getLocation(), Sound.GLASS, 1, 1);
            Util.VANISH.add(player);

            User user = plugin.getUserManager().getUser(player.getUniqueId());
            if (user.hasQuitMessage()) {
                Bukkit.broadcastMessage("§8[§c-§8] §7" + user.getQuitMessage().replace('&', '§').replace("§k", "&k"));
            }
        } else {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.hasPermission("myth.team")) {
                    onlinePlayer.showPlayer(player);
                }
            }
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu bist nun nicht mehr Vanish, jeder sieht dich.");
            player.playSound(player.getLocation(), Sound.GLASS, 1, 1);
            Util.VANISH.remove(player);

            User user = plugin.getUserManager().getUser(player.getUniqueId());
            if (user.hasJoinMessage()) {
                Bukkit.broadcastMessage("§8[§a+§8] §7" + user.getJoinMessage().replace('&', '§').replace("§k", "&k"));
            }
        }

    }
}
