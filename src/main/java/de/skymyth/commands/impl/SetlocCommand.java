package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.location.model.Position;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetlocCommand extends MythCommand {

    public SetlocCommand(SkyMythPlugin plugin) {
        super("setloc", "myth.setloc", new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        // /setloc

        if(args.length == 1) {

            String name = args[0];
            Position position = new Position(name, player.getLocation(), false);
            plugin.getLocationManager().savePosition(position);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Die Position §e" + name + " §7wurde gesetzt.");
            return;
        }
        player.sendMessage(SkyMythPlugin.PREFIX + "§cVerwende: /setloc <name>");

    }
}
