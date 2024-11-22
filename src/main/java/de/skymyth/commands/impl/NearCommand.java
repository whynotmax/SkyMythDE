package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NearCommand extends MythCommand {

    public NearCommand(SkyMythPlugin plugin) {
        super("near", "myth.near", null, plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        for (Entity nearbyEntity : player.getWorld().getNearbyEntities(player.getLocation(), 20, 20, 20)) {
            if (nearbyEntity instanceof Player minecraftPlayer) {
                player.sendMessage(SkyMythPlugin.PREFIX + minecraftPlayer.getName() + " ist §e" + nearbyEntity.getLocation().distance(player.getLocation()) + " §7Blöcke weit weg.");
            }
        }

    }
}
