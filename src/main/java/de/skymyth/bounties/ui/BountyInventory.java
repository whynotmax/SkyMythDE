package de.skymyth.bounties.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.utility.UUIDFetcher;
import de.skymyth.utility.item.ItemBuilder;
import de.skymyth.utility.item.SkullCreator;
import de.skymyth.utility.pagination.Pagination;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class BountyInventory extends AbstractInventory {

    SkyMythPlugin plugin;
    Pagination<ItemStack> pagination;
    int page;

    public BountyInventory(UUID playerUniqueId) {
        super("Kopfgeld: Menü", 54);

        defaultInventory();

        this.page = 0;
        this.pagination = new Pagination<>(27);

        for (Map.Entry<UUID, Long> entry : plugin.getBountyManager().sortedBounties().entrySet()) {
            UUID target = entry.getKey();
            long reward = entry.getValue();

            ItemBuilder itemBuilder = new ItemBuilder(new ItemBuilder(SkullCreator.itemFromUuid(target)));
            itemBuilder.setName("§e" + UUIDFetcher.getName(target));
            itemBuilder.lore(
                    "§r",
                    "§7Kopfgeld: §e" + reward + " ⛃",
                    "§r"
            );
            if (target.equals(playerUniqueId)) {
                itemBuilder.glow();
            }
            pagination.addItem(itemBuilder);
        }

    }

    public void update(int page) {
        this.page = page;
        setItem(53, new ItemBuilder(Material.ARROW).setName("§7Nächste Seite").lore(
                "§7Klicke hier um zur nächsten Seite zu gelangen"
        ), event -> {
            if (this.pagination.isLastPage(this.page)) {
                return;
            }
            update(this.page + 1);
        });

        setItem(45, new ItemBuilder(Material.ARROW).setName("§7Vorherige Seite").lore(
                "§7Klicke hier um zur vorherigen Seite zu gelangen"
        ), event -> {
            if (this.pagination.isFirstPage(this.page)) {
                return;
            }
            update(this.page - 1);
        });

        int slot = 10;
        for (ItemStack itemStack : this.pagination.getItems(this.page)) {
            setItem(slot, itemStack);
            slot++;
            if (slot == 17) {
                slot = 19;
            }
            if (slot == 26) {
                slot = 28;
            }
            if (slot == 35) {
                slot = 37;
            }
        }

    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
