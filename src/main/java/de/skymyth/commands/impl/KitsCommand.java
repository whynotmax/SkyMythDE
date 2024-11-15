package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.kit.ui.KitMainInventory;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitsCommand extends MythCommand {

    public KitsCommand(SkyMythPlugin plugin) {
        super("kits", null, List.of("kit"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        plugin.getInventoryManager().openInventory(player, new KitMainInventory(this.plugin));
    }

}
