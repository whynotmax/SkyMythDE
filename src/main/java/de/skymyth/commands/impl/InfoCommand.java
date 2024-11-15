package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class InfoCommand extends MythCommand {

    public InfoCommand(SkyMythPlugin plugin) {
        super("info", "myth.info", new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        if (args.length == 1) {
            Player targetPlayer = Bukkit.getPlayer(args[0]);

            if (targetPlayer == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler ist nicht online.");
                return;
            }

            player.sendMessage("§8§m------------------------------------------------------§r");
            player.sendMessage("§r");
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Spielername: §e" + targetPlayer.getName());
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Ping: §e" + ((CraftPlayer) targetPlayer).getHandle().ping + "§7§oms");
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Host: §e" + targetPlayer.getAddress().getHostString());
            player.sendMessage("§r");
            player.sendMessage("§8§m------------------------------------------------------§r");


            return;
        }
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /Info <spieler>");
    }
}
