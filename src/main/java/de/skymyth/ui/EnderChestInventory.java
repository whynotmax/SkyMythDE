package de.skymyth.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.user.model.User;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EnderChestInventory extends AbstractInventory {

    int page;
    User user;
    boolean isTrusted;
    SkyMythPlugin plugin;

    public EnderChestInventory(User user, Player toOpen, SkyMythPlugin plugin) {
        super("Enderchest", 36);
        this.user = user;
        this.page = 0;
        this.isTrusted = (user.getEnderChest().getTrustedPlayers().contains(toOpen.getUniqueId()) || user.getUniqueId().equals(toOpen.getUniqueId()) || toOpen.hasPermission("myth.enderchest.bypass"));
        this.plugin = plugin;

        update(0);
    }

    private void update(int newPage) {
        saveCurrentPage();
        this.page = newPage;

        for (int i = 0; i < 26; i++) {
            setItem(i, new ItemBuilder(Material.AIR));
        }

        Map<Integer, ItemStack> pageItems = user.getEnderChest().getPages().size() > (page+1) ? user.getEnderChest().getPages().get(page) : new HashMap<>();
        if (pageItems != null) {
            for (Map.Entry<Integer, ItemStack> entry : pageItems.entrySet()) {
                setItem(entry.getKey(), entry.getValue(), event -> event.setCancelled(!isTrusted));
            }
        }

        for (int i = 27; i < 36; i++) {
            setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDataId(15).setName("§7"), event -> event.setCancelled(true));
        }

        setItem(27, new ItemBuilder(Material.ARROW).setName("§eVorherige Seite"), event -> {
            event.setCancelled(true);
            if (page > 0) {
                update(page - 1);
            } else {
                Player player = (Player) event.getWhoClicked();
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu bist bereits auf der ersten Seite.");
            }
        });

        setItem(35, new ItemBuilder(Material.ARROW).setName("§eNächste Seite"), event -> {
            event.setCancelled(true);
            if (page + 1 < user.getEnderChest().getAquiredPages()) {
                update(page + 1);
            } else {
                Player player = (Player) event.getWhoClicked();
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu bist bereits auf der letzten Seite.");
            }
        });
    }

    private void saveCurrentPage() {
        Map<Integer, ItemStack> pageItems = user.getEnderChest().getPages().size() > page ? user.getEnderChest().getPages().get(page) : new HashMap<>();
        for (int i = 0; i < 26; i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                pageItems.put(i, item);
            } else {
                pageItems.remove(i);
            }
        }
        if (user.getEnderChest().getPages().size() > page) {
            user.getEnderChest().getPages().set(page, pageItems);
        } else {
            user.getEnderChest().getPages().add(page, pageItems);
        }
        plugin.getUserManager().saveUser(user);
    }

    @Override
    public void close(InventoryCloseEvent event) {
        saveCurrentPage();
        event.getPlayer().sendMessage(SkyMythPlugin.PREFIX + "§aSeite " + (page + 1) + " gespeichert.");
    }
}