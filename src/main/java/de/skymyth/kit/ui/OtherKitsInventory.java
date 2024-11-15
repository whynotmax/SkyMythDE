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
            ItemBuilder displayItem = new ItemBuilder(kit.getDisplayItem());
            displayItem.setName((user.hasCooldown("kit_" + kit.getName().toLowerCase()) ? "§c" : (kit.isEnabled() ? "§a" : "§c")) + kit.getName());
            displayItem.lore(
                    (kit.isEnabled() ? "§a" : "§c") + "§o" + (kit.isEnabled() ? "aktiviert" : "deaktiviert"),
                    "§r",
                    (user.hasCooldown("kit_" + kit.getName().toLowerCase()) ? "§c" : "§a") + (user.hasCooldown("kit_" + kit.getName().toLowerCase()) ? "Warte noch " + TimeUtil.beautifyTime(user.getCooldown("kit_" + kit.getName().toLowerCase()).getRemainingTime(), TimeUnit.MILLISECONDS, true, true) : "Klicke§7, um das Kit zu erhalten")
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
                Kit kit = plugin.getKitManager().getKitByName(itemStack.getItemMeta().getDisplayName().replaceFirst("§[a-f0-9]", ""));
                if (kit == null) {
                    return;
                }
                kit.giveTo(user);
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
