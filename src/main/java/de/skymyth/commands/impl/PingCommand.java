package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PingCommand extends MythCommand {

    public PingCommand(SkyMythPlugin plugin) {
        super("ping", null, new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Dein aktueller Ping: §e" + this.formatPing(((CraftPlayer) player).getHandle().ping));
            return;
        }
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Spieler §e" + args[0] + "§c ist nicht online.");
            return;
        }
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Aktueller Ping von §e" + target.getName() + "§7: §e" + this.formatPing(((CraftPlayer) target).getHandle().ping));
    }

    private String formatPing(int ping) {
        if (ping < 30) {
            return "§2" + ping + "§oms §8(§7sehr gut§8)";
        }
        if (ping < 50) {
            return "§a" + ping + "§oms §8(§7gut§8)";
        }
        if (ping < 100) {
            return "§e" + ping + "§oms §8(§7mittelmäßig§8)";
        }
        if (ping < 150) {
            return "§c" + ping + "§oms §8(§7schlecht§8)";
        }
        return "§4" + ping + "§oms §8(§7sehr schlecht§8)";
    }
}
