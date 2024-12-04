package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PurgeCommand extends MythCommand {

    public PurgeCommand(SkyMythPlugin plugin) {
        super("purge", "skymyth.purge", new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        int chestsCleared = 0;
        for (int i = 0; i < 50; i++) {
            Block block = player.getLocation().getWorld().getBlockAt(i, i, i);

            Chest chest = (Chest) block.getState();

            if (chest.getInventory().getContents().length > 0) {
                chest.getInventory().clear();
                chest.update();
                chestsCleared++;
            }
        }
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Es wurden §e" + chestsCleared + " §7Kisten geleert.");
    }
}
