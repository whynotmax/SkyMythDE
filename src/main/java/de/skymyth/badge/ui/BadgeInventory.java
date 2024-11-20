package de.skymyth.badge.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.badge.model.Badge;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.user.model.User;
import de.skymyth.utility.item.ItemBuilder;
import de.skymyth.utility.pagination.Pagination;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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

        List<Badge> badgesSortedByOwnership = plugin.getBadgeManager().getBadges();

        pagination = new Pagination<>(28);

        User user = plugin.getUserManager().getUser(playerUUID);
        for (Badge badge : badgesSortedByOwnership) {
            ItemBuilder badgeItem = new ItemBuilder(Material.PAPER);
            badgeItem.setName("§8[§e" + badge.getColor() + badge.getCharacter() + "§8] §7Badge");
            badgeItem.glow(user.getSelectedBadge().equalsIgnoreCase(badge.getName()));
            badgeItem.lore(
                    "§8Badge-ID: " + badge.getName(),
                    "§r",
                    "§7§o" + badge.getDescription(),
                    "§r",
                    "§7Besitzer: §e" + badge.getOwners().size(),
                    "§7Erstellt um: §e" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(badge.getCreationDate()),
                    "§7Erstellt von: §e" + plugin.getServer().getOfflinePlayer(badge.getCreator()).getName(),
                    "§r",
                    (badge.getOwners().contains(playerUUID) ? "§a" : "§c") + (badge.getOwners().contains(playerUUID) ? "Du besitzt dieses Badge." : "Du besitzt dieses Badge nicht.")
            );
            pagination.addItem(badgeItem);
        }

        update(0);

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

        this.setItem(49, new ItemBuilder(Material.BARRIER).setName("§cBadge entfernen").lore("§7Klicke, um deine aktive Badge zurückzusetzen."), event -> {
            Player player = (Player) event.getWhoClicked();
            User user = plugin.getUserManager().getUser(player.getUniqueId());
            if (user.getSelectedBadge() == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu hast keine aktive Badge ausgewählt.");
                return;
            }
            user.setSelectedBadge(null);
            plugin.getUserManager().saveUser(user);
            player.sendMessage(SkyMythPlugin.PREFIX + "§aDu hast deine aktive Badge zurückgesetzt.");
        });

        int slot = 10;
        for (ItemStack itemStack : pagination.getItems(page)) {
            this.setItem(slot, itemStack, event -> {
                Player player = (Player) event.getWhoClicked();
                User user = plugin.getUserManager().getUser(player.getUniqueId());
                Badge badge = plugin.getBadgeManager().getBadge(itemStack.getItemMeta().getLore().get(0).replaceFirst("§8Badge-ID: ", ""));
                if (!badge.getOwners().contains(player.getUniqueId())) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu besitzt dieses Badge nicht.");
                    return;
                }
                if (user.getSelectedBadge() != null && user.getSelectedBadge().equalsIgnoreCase(badge.getName())) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu hast dieses Badge bereits ausgewählt.");
                    return;
                }
                user.setSelectedBadge(badge.getName());
                plugin.getUserManager().saveUser(user);
                player.sendMessage(SkyMythPlugin.PREFIX + "§aDu hast das Badge §e" + badge.getName() + " §aausgewählt.");
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
