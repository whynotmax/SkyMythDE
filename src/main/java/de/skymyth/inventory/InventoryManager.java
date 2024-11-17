package de.skymyth.inventory;

import de.skymyth.inventory.impl.AbstractInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryManager implements Listener {

    JavaPlugin plugin;
    Map<UUID, AbstractInventory> inventories;

    public InventoryManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.inventories = new HashMap<>();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    public void openInventory(Player player, AbstractInventory inventory) {
        this.inventories.put(player.getUniqueId(), inventory);
        player.openInventory(inventory.getInventory());
    }

    public void closeInventory(Player player) {
        player.closeInventory();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (this.inventories.containsKey(player.getUniqueId())) {
            AbstractInventory inventory = this.inventories.get(player.getUniqueId());
            if (event.getClickedInventory() != null && event.getClickedInventory().equals(inventory.getInventory())) {
                event.setCancelled(!event.getClickedInventory().getTitle().contains("Enderchest"));
                inventory.handleClickEvent(event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (this.inventories.containsKey(player.getUniqueId())) {
            AbstractInventory inventory = this.inventories.get(player.getUniqueId());
            if (event.getInventory() != null && event.getInventory().equals(inventory.getInventory())) {
                inventory.close(event);
                this.inventories.remove(player.getUniqueId());
            }
        }
    }

}