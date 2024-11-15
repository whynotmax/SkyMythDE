package de.skymyth.kit.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

public class KitMainInventory extends AbstractInventory {

    SkyMythPlugin plugin;

    public KitMainInventory(SkyMythPlugin plugin) {
        super("Kits: Menü", InventoryType.HOPPER);
        this.plugin = plugin;

        setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDataId(15).setName("§r"));
        setItem(2, new ItemBuilder(Material.STAINED_GLASS_PANE).setDataId(15).setName("§r"));
        setItem(4, new ItemBuilder(Material.STAINED_GLASS_PANE).setDataId(15).setName("§r"));

        setItem(1, new ItemBuilder(Material.CHEST).setName("§aRangspezifische Kits").lore("§7§oHier kannst du dir dein PvP-Kit auswählen!"), event -> {
            this.plugin.getInventoryManager().openInventory((Player) event.getWhoClicked(), new RankSpecificKitsInventory(plugin, plugin.getUserManager().getUser(event.getWhoClicked().getUniqueId())));
        });

        setItem(3, new ItemBuilder(Material.CHEST).setName("§aSonstige Kits").lore("§7§oHier kannst du andere Kits kaufen und erhalten!"), event -> {
            this.plugin.getInventoryManager().openInventory((Player) event.getWhoClicked(), new OtherKitsInventory(plugin, plugin.getUserManager().getUser(event.getWhoClicked().getUniqueId())));
        });
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
