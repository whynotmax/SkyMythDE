package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.location.model.Position;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SetlocCommand extends MythCommand {

    public SetlocCommand(SkyMythPlugin plugin) {
        super("setloc", "myth.setloc", new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        // /setloc

        if (args.length == 1) {

            String name = args[0];

            if(name.equalsIgnoreCase("spawn")) {
                player.getWorld().setSpawnLocation((int) player.getLocation().getX(), (int) player.getLocation().getY(), (int) player.getLocation().getZ());
            }

            Position position = new Position(name, player.getLocation(), false);
            plugin.getLocationManager().savePosition(position);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Die Position §e" + name + " §7wurde gesetzt.");
            return;
        }
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /setloc <name>");

    }
}
