package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ChatclearCommand extends MythCommand {

    public ChatclearCommand(SkyMythPlugin plugin) {
        super("chatclear", "myth.cc", new ArrayList<>() {{
            add("cc");
            add("clearchat");
        }}, plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.hasPermission("myth.team")) {
                for (int i = 0; i < 50; i++) {
                    onlinePlayer.sendMessage("§" + String.valueOf(i).charAt(0));
                }
            }

            onlinePlayer.sendMessage(SkyMythPlugin.PREFIX + "§7Der Chat wurde von §e" + player.getName() + " §7geleert.");
            onlinePlayer.playSound(onlinePlayer.getLocation().add(0, 10, 0), Sound.ENDERDRAGON_HIT, 1, 1);

        }
    }
}
