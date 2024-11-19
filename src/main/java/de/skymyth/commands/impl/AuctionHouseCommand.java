package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.auctionhouse.ui.AuctionHouseMainInventory;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class AuctionHouseCommand extends MythCommand {

    public AuctionHouseCommand(SkyMythPlugin plugin) {
        super("auktionshaus", null, List.of("ah", "auctionhouse"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        plugin.getInventoryManager().openInventory(player, new AuctionHouseMainInventory(plugin));
    }
}
