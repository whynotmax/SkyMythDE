package de.skymyth.kit.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.kit.model.Kit;
import de.skymyth.user.model.User;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.item.ItemBuilder;
import de.skymyth.utility.pagination.Pagination;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

public class OtherKitsInventory extends AbstractInventory {

    Pagination<ItemStack> pagination;
    SkyMythPlugin plugin;
    User user;
    int page;

    public OtherKitsInventory(SkyMythPlugin plugin, User user) {
        super("Kits: Sonstige Kits", 54);
        this.plugin = plugin;
        this.user = user;

        defaultInventory();

        pagination = new Pagination<>(28);

        for (Kit kit : plugin.getKitManager().getOtherKits()) {
            if (kit.isSystemOnly()) continue;
            ItemBuilder displayItem = new ItemBuilder(kit.getDisplayItem());
            displayItem.setName("§7Kit§8: §e" + kit.getName());
            displayItem.lore(
                    "§7Dieses Kit ist im Moment " + (kit.isEnabled() ? " §aaktiviert." : " §cdeaktiviert."),
                    "",
                    (user.isOnCooldown("kit" + kit.getName().toLowerCase()) ?
                            "§cBitte warte noch " + TimeUtil.beautifyTime(user.getCooldown("kit_" + kit.getName().toLowerCase()).getRemainingTime(), TimeUnit.MILLISECONDS, true, true)
                            :
                            "§7Du kannst das Kit jetzt sofort abholen"),
                    "§r",
                    "§7Rechtsklick, um die Kit-Vorschau zu sehen.",
                    "§7Linksklick, um das Kit zu erhalten."
            );
            pagination.addItem(displayItem);
        }

        update(0);

    }

    @Override
    public void close(InventoryCloseEvent event) {
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
            if (pagination.getPages().size() == page + 1) {
                return;
            }
            update(page + 1);
        });

        int i = 10;
        for (ItemStack itemStack : pagination.getItems(newPage)) {
            this.setItem(i, itemStack, event -> {
                Player player = (Player) event.getWhoClicked();
                Kit kit = plugin.getKitManager().getKitByName(itemStack.getItemMeta().getDisplayName().replaceAll("§7Kit§8: §e", ""));
                if (kit == null) {
                    return;
                }
                if (event.getAction().equals(InventoryAction.PICKUP_HALF)) {
                    plugin.getInventoryManager().openInventory(player, new KitPreviewInventory(plugin, user, kit));
                    return;
                }
                kit.giveTo(user, plugin);
                update(page);
            });
            i++;
            if (i == 17) {
                i = 19;
            }
            if (i == 26) {
                i = 28;
            }
            if (i == 35) {
                i = 37;
            }
        }
    }
}
