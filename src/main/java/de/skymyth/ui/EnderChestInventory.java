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
        this.page = newPage;

        Map<Integer, ItemStack> pageItems = user.getEnderChest().getPages().get(page);
        if (pageItems == null) {
            //TODO: Add empty page
        } else {
            for (Map.Entry<Integer, ItemStack> entry : pageItems.entrySet()) {
                setItem(entry.getKey(), entry.getValue(), event -> event.setCancelled(!isTrusted));
            }
        }

        for (int i = 26; i < 36; i++) {
            setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDataId(15).setName("§7"), event -> event.setCancelled(true));
        }

        setItem(27, new ItemBuilder(Material.ARROW).setName("§eVorherige Seite"), event -> {
            if (page > 0) {
                for (int i = 0; i < 26; i++) {
                    ItemStack item = inventory.getItem(i);
                    if (item == null) {
                        continue;
                    }
                    Map<Integer, ItemStack> pageItemsSwitch = user.getEnderChest().getPages().get(page);
                    if (pageItemsSwitch == null) {
                        pageItemsSwitch = new HashMap<>();
                    }
                    pageItemsSwitch.put(i, item);
                    user.getEnderChest().getPages().add(page, pageItemsSwitch);
                }
                plugin.getUserManager().saveUser(user);
                update(page - 1);
                return;
            }
            Player player = (Player) event.getWhoClicked();
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu bist bereits auf der ersten Seite.");
        });

        setItem(35, new ItemBuilder(Material.ARROW).setName("§eNächste Seite"), event -> {
            if (page < user.getEnderChest().getAquiredPages()) {
                for (int i = 0; i < 26; i++) {
                    ItemStack item = inventory.getItem(i);
                    if (item == null) {
                        continue;
                    }
                    Map<Integer, ItemStack> pageItemsSwitch = user.getEnderChest().getPages().get(page);
                    if (pageItemsSwitch == null) {
                        pageItemsSwitch = new HashMap<>();
                    }
                    pageItemsSwitch.put(i, item);
                    user.getEnderChest().getPages().add(page, pageItemsSwitch);
                }
                plugin.getUserManager().saveUser(user);
                update(page + 1);
                return;
            }
            Player player = (Player) event.getWhoClicked();
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu bist bereits auf der letzten Seite.");
        });
    }

    @Override
    public void close(InventoryCloseEvent event) {
        for (int i = 0; i < 26; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null) {
                continue;
            }
            Map<Integer, ItemStack> pageItemsSwitch = user.getEnderChest().getPages().get(page);
            if (pageItemsSwitch == null) {
                pageItemsSwitch = new HashMap<>();
            }
            pageItemsSwitch.put(i, item);
            user.getEnderChest().getPages().add(page, pageItemsSwitch);
        }
        plugin.getUserManager().saveUser(user);
    }
}
