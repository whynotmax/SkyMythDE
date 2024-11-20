package de.skymyth.inventory.impl;

import de.skymyth.utility.item.ItemBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractInventory {

    String title;
    InventoryType type;
    int size; // 9, 18, 27, 36, 45, 54
    Map<Integer, Consumer<InventoryClickEvent>> clickActions;
    Inventory inventory;

    public AbstractInventory(String title, int size) {
        this.clickActions = new HashMap<>();
        this.title = title;
        this.size = size;
        this.type = InventoryType.CHEST;
        this.inventory = Bukkit.createInventory(null, size, title);
    }

    public AbstractInventory(String title, InventoryType type) {
        this.clickActions = new HashMap<>();
        this.title = title;
        this.type = type;
        this.size = type.getDefaultSize();
        this.inventory = Bukkit.createInventory(null, type, title);
    }

    public abstract void close(InventoryCloseEvent event);

    public void setItem(int slot, ItemStack item) {
        this.setItem(slot, item, null);
    }

    public void update() {
    }

    public void setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> clickAction) {
        this.inventory.setItem(slot, item);
        if (clickAction != null) {
            this.clickActions.put(slot, clickAction);
        }
    }

    public void fill(ItemStack item) {
        this.fill(item, null);
    }

    public void fill(ItemStack item, Consumer<InventoryClickEvent> clickAction) {
        for (int i = 0; i < this.size; i++) {
            this.setItem(i, item, clickAction);
        }
    }

    public void fillBorders(ItemStack item) {
        this.fillBorders(item, null);
    }

    public void fillBorders(ItemStack item, Consumer<InventoryClickEvent> clickAction) {
        for (int i = 0; i < this.size; i++) {
            if (i < 9 || i >= this.size - 9 || i % 9 == 0 || i % 9 == 8) {
                this.setItem(i, item, clickAction);
            }
        }
    }

    public void defaultInventory() {
        this.fill(new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).setName("§0"));
        this.fillBorders(new ItemBuilder(Material.STAINED_GLASS_PANE).durability(15).setName("§0"));
    }

    public void clear() {
        this.inventory.clear();
        this.clickActions.clear();
    }

    public void handleClickEvent(InventoryClickEvent event) {
        if (this.clickActions.containsKey(event.getSlot())) {
            this.clickActions.get(event.getSlot()).accept(event);
            return;
        } else event.setCancelled(true);
    }

}