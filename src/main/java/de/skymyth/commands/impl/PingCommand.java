package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PingCommand extends MythCommand {

    public PingCommand(SkyMythPlugin plugin) {
        super("ping", null, new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        player.sendMessage(SkyMythPlugin.PREFIX + "§7Dein aktueller Ping: §e" + ((CraftPlayer)player).getHandle().ping + "§7§oms");
    }
}
