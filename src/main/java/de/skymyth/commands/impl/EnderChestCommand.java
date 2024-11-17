package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class EnderChestCommand extends MythCommand {

    public EnderChestCommand(SkyMythPlugin plugin) {
        super("enderchest", null, List.of("lager", "ec"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        player.openInventory(player.getEnderChest());
    }
}
