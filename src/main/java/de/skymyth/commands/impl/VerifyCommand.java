package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class VerifyCommand extends MythCommand {

    public VerifyCommand(SkyMythPlugin plugin) {
        super("verify", null, List.of("verifizieren"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

    }
}
