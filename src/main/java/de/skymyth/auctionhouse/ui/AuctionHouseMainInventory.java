package de.skymyth.auctionhouse.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.auctionhouse.model.AuctionHouseItem;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.UUIDFetcher;
import de.skymyth.utility.item.ItemBuilder;
import de.skymyth.utility.pagination.Pagination;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AuctionHouseMainInventory extends AbstractInventory {

    Pagination<AuctionHouseItem> pagination;
    int page;

    public AuctionHouseMainInventory(SkyMythPlugin plugin) {
        super("Auktionen: Alle", 54);

        defaultInventory();

        this.page = 0;
        this.pagination = new Pagination<>(0, 28);

        plugin.getAuctionHouseManager().getAuctionHouseItems().forEach(this.pagination::addItem);

        update(0);
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }

    private void update(int newPage) {
        this.page = newPage;

        defaultInventory();

        setItem(47, new ItemBuilder(Material.HOPPER).setName("§aItems filtern").lore(
                "§7Filtere die Items nach:",
                "§8- §7Preis (günstigste zuerst)",
                "§8- §7Preis (teuerste zuerst)",
                "§8- §7Ablaufdatum (früheste zuerst)",
                "§8- §7Ablaufdatum (späteste zuerst)"
        ));

        setItem(48, new ItemBuilder(Material.ANVIL).setName("§aItems suchen").lore(
                "§7Suche Items nach einem bestimmten Namen"
        ));

        setItem(50, new ItemBuilder(Material.CHEST).setName("§cDeine abgelauften Auktionen").lore(
                "§7Hier findest du alle deine abgelaufenen Auktionen"
        ));

        setItem(51, new ItemBuilder(Material.EMERALD).setName("§aAuktion starten").lore(
                "§7Starte eine neue Auktion"
        ));

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
        for (AuctionHouseItem item : this.pagination.getItems(this.page)) {
            ItemBuilder itemBuilder = new ItemBuilder(item.getItemStack());
            itemBuilder.setName(item.getItemStack().getItemMeta().hasDisplayName() ? item.getItemStack().getItemMeta().getDisplayName() : item.getItemStack().getType().name());
            itemBuilder.lore(
                    "§7Verkäufer: §e" + UUIDFetcher.getName(item.getSeller()),
                    "§7Preis: §e" + NumberFormat.getInstance(Locale.GERMAN).format(item.getPrice()).replace(",", ".") + " Coins",
                    "§7Kategorie: §e" + item.getCategory().getName(),
                    "§7Ablaufdatum: §e" + TimeUtil.beautifyTime(item.getRemainingTime(), TimeUnit.MILLISECONDS, true, true)
            );
            slot++;
            if (slot == 17) {
                slot = 19;
            } else if (slot == 26) {
                slot = 28;
            } else if (slot == 35) {
                slot = 37;
            }
        }
    }
}
