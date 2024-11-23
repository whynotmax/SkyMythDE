package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
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
                .addTrophies(500);
        plugin.getUserManager().getUser(player.getUniqueId())
                .addKill();


        //player.getInventory().addItem(plugin.getProtectorManager().getProtectorItem());

        /*
        Block block = player.getLocation().getBlock();
        block.setType(Material.CHEST);
        Chest chest = (Chest) block.getState();

        chest.getInventory().setItem(3, new ItemStack(Material.SPONGE));
        chest.getInventory().setItem(6, new ItemStack(Material.TNT));
        chest.getInventory().setItem(13, new ItemStack(Material.STICK));

         */
        player.getInventory().addItem(plugin.getBaseProtectorManager().getBaseProtectorItem());

    }
}
