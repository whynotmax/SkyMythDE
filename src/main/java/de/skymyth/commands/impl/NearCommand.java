package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class NearCommand extends MythCommand {

    public NearCommand(SkyMythPlugin plugin) {
        super("near", "myth.near", List.of("nähe"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        int count = 0;
        for (Entity nearbyEntity : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (nearbyEntity == player) continue;
            count++;
            if(count == 0) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cEs befinden sich keine Spieler in deiner Nähe.");
                return;
            }
            if (nearbyEntity instanceof Player minecraftPlayer) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§e" + minecraftPlayer.getName() + " ist §e" + Math.round(nearbyEntity.getLocation().distance(player.getLocation())) + " §7Blöcke weit weg.");
            }
        }

    }
}
