package de.skymyth.kit.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.kit.model.Kit;
import de.skymyth.user.model.User;
import de.skymyth.utility.item.ItemBuilder;
import de.skymyth.utility.pagination.Pagination;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class KitPreviewInventory extends AbstractInventory {

    Pagination<ItemStack> pagination;
    SkyMythPlugin plugin;
    User user;
    Kit kit;
    int page;

    public KitPreviewInventory(SkyMythPlugin plugin, User user, Kit kit) {
        super("Kits: " + kit.getName() + " (Vorschau)", 54);
        this.plugin = plugin;
        this.user = user;
        this.kit = kit;

        defaultInventory();

        pagination = new Pagination<>(28);

        for (ItemStack itemStack : (kit.getItems())) {
            pagination.addItem(itemStack);
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
            this.setItem(i, new ItemBuilder(itemStack).setName("§7Kit§8: §e" + kit.getName()));
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
