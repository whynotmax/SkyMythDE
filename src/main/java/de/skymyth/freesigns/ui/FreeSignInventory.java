package de.skymyth.freesigns.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.freesigns.model.FreeSign;
import de.skymyth.freesigns.repository.FreeSignRepository;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class FreeSignInventory extends AbstractInventory {

    SkyMythPlugin plugin;
    FreeSign sign;

    public FreeSignInventory(SkyMythPlugin plugin, FreeSign sign) {
        super("Free", 27);
        this.plugin = plugin;
        this.sign = sign;

        defaultInventory();

        int slot = 10;
        for (int i = 0; i < 7; i++) {
            int finalSlot = slot;
            setItem(slot, sign.getItemStack().clone(), event -> {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                player.getInventory().addItem(sign.getItemStack().clone());
                player.playSound(player.getLocation(), "ENTITY_ITEM_PICKUP", 1.0F, 1.0F);
                setItem(finalSlot, new ItemBuilder(Material.BARRIER).setName("§c-/-"), event2 -> event2.setCancelled(true));
            });
            slot++;
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
