package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreezeCommand extends MythCommand {

    Map<Player, Location> lastLocation;

    public FreezeCommand(SkyMythPlugin plugin) {
        super("freeze", "myth.freeze", new ArrayList<>(), plugin);
        this.lastLocation = new HashMap<>();
    }

    @Override
    public void run(Player player, String[] args) {

        if(args.length == 1) {
            Player targetPlayer = Bukkit.getPlayer(args[0]);

            if(targetPlayer == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler wurde nicht gefunden.");
                return;
            }

            if(targetPlayer.isOp()) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst diesen Spieler nicht freezen.");
                return;
            }

            if(!Util.FREEZE.contains(targetPlayer)) {
                Util.FREEZE.add(targetPlayer);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + targetPlayer.getName() + " §7ist nun eingefroren.");
                this.lastLocation.put(targetPlayer, targetPlayer.getLocation());
                targetPlayer.teleport(plugin.getLocationManager().getPosition("spawn").getLocation());
            } else {
                Util.FREEZE.remove(targetPlayer);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + targetPlayer.getName() + " §7ist nun nicht mehr eingefroren.");
                if(this.lastLocation.get(targetPlayer) != null) {
                    targetPlayer.teleport(this.lastLocation.get(targetPlayer));
                }
            }
            return;
        }
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /freeze <spieler>");

    }
}
