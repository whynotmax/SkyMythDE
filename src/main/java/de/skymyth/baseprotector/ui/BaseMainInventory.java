package de.skymyth.baseprotector.ui;

import de.skymyth.inventory.impl.AbstractInventory;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class BaseMainInventory extends AbstractInventory {

    public BaseMainInventory() {
        super("Base: Menü", 36);

        this.defaultInventory();
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
