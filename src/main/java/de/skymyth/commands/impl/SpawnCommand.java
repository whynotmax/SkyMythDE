package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.TeleportUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SpawnCommand extends MythCommand {


    public SpawnCommand(SkyMythPlugin plugin) {
        super("spawn", null, new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        TeleportUtil.createTeleportation(plugin, player, plugin.getLocationManager().getPosition("spawn").getLocation(), "Spawn");


    }
}
