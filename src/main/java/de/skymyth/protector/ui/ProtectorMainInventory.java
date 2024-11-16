package de.skymyth.protector.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.kit.ui.OtherKitsInventory;
import de.skymyth.kit.ui.RankSpecificKitsInventory;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

public class ProtectorMainInventory extends AbstractInventory {

    SkyMythPlugin plugin;

    public ProtectorMainInventory(SkyMythPlugin plugin) {
        super("Protector: Menü", InventoryType.CHEST);
        this.plugin = plugin;


    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}