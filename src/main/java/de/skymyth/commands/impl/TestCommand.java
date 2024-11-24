package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public class TestCommand extends MythCommand {


    public TestCommand(SkyMythPlugin plugin) {
        super("test", "myth.op", new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

    }
}
