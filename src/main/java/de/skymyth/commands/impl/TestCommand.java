package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.giveaway.impl.ItemGiveaway;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TestCommand extends MythCommand {

    public TestCommand(SkyMythPlugin plugin) {
        super("test", null, new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        //new ItemGiveaway(plugin, new ItemBuilder(Material.STONE).amount(300)).run();
        plugin.getUserManager().getUser(player.getUniqueId())
                .addKill();

        player.getInventory().addItem(plugin.getProtectorManager().getProtectorItem());

    }
}
