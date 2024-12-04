package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.location.model.Position;
import de.skymyth.utility.TeleportUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class MineCommand extends MythCommand {

    public MineCommand(SkyMythPlugin plugin) {
        super("mine", null, List.of(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        player.sendMessage(SkyMythPlugin.PREFIX + "§cDie Mine wird bald hinzugefügt. Bitte habe noch etwas Geduld.");
//        Position position = plugin.getLocationManager().getPosition("mine");
//        if (position == null) {
//            player.sendMessage(SkyMythPlugin.PREFIX + "§cDie Mine wurde noch nicht gesetzt.");
//            return;
//        }
//        TeleportUtil.createTeleportation(plugin, player, position.getLocation(), "Mine");
    }
}
