package de.skymyth.badge.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.badge.model.Badge;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.utility.item.ItemBuilder;
import de.skymyth.utility.pagination.Pagination;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

public class BadgeInventory extends AbstractInventory {

    SkyMythPlugin plugin;

    Pagination<ItemStack> pagination;
    int page;

    public BadgeInventory(SkyMythPlugin plugin, UUID playerUUID) {
        super("Badges: Menü", 54);
        this.plugin = plugin;

        defaultInventory();

        List<Badge> badgesSortedByOwnership = plugin.getBadgeManager().getBadgesSortedByOwnership(playerUUID);

        pagination = new Pagination<>(28);

        for (Badge badge : badgesSortedByOwnership) {
            ItemBuilder badgeItem = new ItemBuilder(Material.PAPER);
            badgeItem.setName("§8[§e" + badge.getCharacter() + "§8] §7Badge");
            badgeItem.lore(
                    "§7§o" + badge.getDescription(),
                    "§r",
                    "§7Besitzer: §e" + badge.getOwners().size(),
                    "§7Erstellt um: §e" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(badge.getCreationDate()),
                    "§7Erstellt von: §e" + plugin.getServer().getOfflinePlayer(badge.getCreator()).getName(),
                    "§r",
                    (badge.getOwners().contains(playerUUID) ? "§a" : "§c") + (badge.getOwners().contains(playerUUID) ? "Du besitzt dieses Badge." : "Du besitzt dieses Badge nicht.")
            );
        }

    }

    private void update(int newPage) {
        this.page = newPage;
        this.clear();
        this.defaultInventory();

        this.setItem(45, new ItemBuilder(Material.ARROW).setName("§cVorherige Seite").lore("§7Klicke, um zur vorherigen Seite zu gelangen."), event -> {
            if (page == 0) {
                return;
            }
            update(page - 1);
        });

        this.setItem(53, new ItemBuilder(Material.ARROW).setName("§aNächste Seite").lore("§7Klicke, um zur nächsten Seite zu gelangen."), event -> {
            if (page == pagination.getPages().size() - 1) {
                return;
            }
            update(page + 1);
        });

        int slot = 10;
        for (ItemStack itemStack : pagination.getItems(page)) {
            this.setItem(slot, itemStack, event -> {
                //TODO: Implement badge click event
            });
            slot++;
            if (slot == 17 || slot == 26 || slot == 35) {
                slot += 2;
            }
        }

    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
