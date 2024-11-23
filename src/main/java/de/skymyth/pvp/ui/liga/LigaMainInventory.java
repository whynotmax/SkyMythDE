package de.skymyth.pvp.ui.liga;

import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class LigaMainInventory extends AbstractInventory {

    public LigaMainInventory() {
        super("Liga: Menü", 27);

        this.defaultInventory();

        setItem(10, new ItemBuilder(Material.RED_SANDSTONE)
                .setName("§6§lBronze Liga")
                .lore(
                        "§7Ab 500 Trophäen befindest",
                        "§7du dich in der §6§lBronze §7Liga.",
                        "",
                        "§7Kämpfe weiter um Belohnungen freizuschalten"
                ));

        setItem(12, new ItemBuilder(Material.STONE)
                .setDataId(6)
                .setName("§7§lSilber Liga")
                .lore(
                        "§7Ab 1.000 Trophäen befindest",
                        "§7du dich in der §7§lSilber §7Liga.",
                        "",
                        "§7Kämpfe weiter um Belohnungen freizuschalten"
                ));

        setItem(14, new ItemBuilder(Material.GOLD_BLOCK)
                .setName("§e§lGold Liga")
                .lore(
                        "§7Ab 1.500 Trophäen befindest",
                        "§7du dich in der §e§lGold §7Liga.",
                        "",
                        "§7Kämpfe weiter um Belohnungen freizuschalten"
                ));

        setItem(16, new ItemBuilder(Material.IRON_BLOCK)
                .setName("§f§lPlatin Liga")
                .lore(
                        "§7Ab 2.000 Trophäen befindest",
                        "§7du dich in der §f§lPlatin §7Liga.",
                        "",
                        "§7Schalte nun exklusive Belohnungen frei"
                ));
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
