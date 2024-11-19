package de.skymyth.auctionhouse.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.auctionhouse.filter.AuctionHouseItemFilter;
import de.skymyth.auctionhouse.filter.impl.AuctionHouseItemExpireDateFilter;
import de.skymyth.auctionhouse.filter.impl.AuctionHouseItemPriceFilter;
import de.skymyth.auctionhouse.filter.impl.result.AuctionHouseItemExpireDateFilterResult;
import de.skymyth.auctionhouse.filter.impl.result.AuctionHouseItemPriceFilterResult;
import de.skymyth.auctionhouse.model.AuctionHouseItem;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.UUIDFetcher;
import de.skymyth.utility.item.ItemBuilder;
import de.skymyth.utility.pagination.Pagination;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AuctionHouseFilterInventory extends AbstractInventory {

    SkyMythPlugin plugin;
    AuctionHouseItemFilter filter;
    Pagination<AuctionHouseItem> pagination;
    int page = 0;

    public AuctionHouseFilterInventory(SkyMythPlugin plugin, AuctionHouseItemFilter filter) {
        super("Auktionen: " + filter.getName(), 54);
        this.plugin = plugin;
        this.filter = filter;

        defaultInventory();

        this.pagination = new Pagination<>(0, 28);

        AuctionHouseItemPriceFilterResult resultPrice = null;
        AuctionHouseItemExpireDateFilterResult resultExpireDate = null;

        if (filter == AuctionHouseItemFilter.PRICE_HIGH_TO_LOW) {
            resultPrice = AuctionHouseItemPriceFilter.startSearching(plugin, filter).search();
        } else if (filter == AuctionHouseItemFilter.PRICE_LOW_TO_HIGH) {
            resultPrice = AuctionHouseItemPriceFilter.startSearching(plugin, filter).search();
        } else if (filter == AuctionHouseItemFilter.EXPIRATION_DATE_HIGH_TO_LOW) {
            resultExpireDate = AuctionHouseItemExpireDateFilter.startSearching(plugin, filter).search();
        } else if (filter == AuctionHouseItemFilter.EXPIRATION_DATE_LOW_TO_HIGH) {
            resultExpireDate = AuctionHouseItemExpireDateFilter.startSearching(plugin, filter).search();
        } else {
            throw new IllegalStateException("Unexpected value: " + filter.getName());
        }

        if (resultPrice != null) {
            resultPrice.getMatches().forEach(pagination::addItem);
        } else if (resultExpireDate != null) {
            resultExpireDate.getMatches().forEach(pagination::addItem);
        }

        update(0);
    }

    private void update(int newPage) {
        this.page = newPage;

        defaultInventory();

        int slot = 10;
        for (AuctionHouseItem auctionHouseItem : pagination.getItems(newPage)) {
            ItemBuilder itemBuilder = new ItemBuilder(auctionHouseItem.getItemStack());
            List<String> lore = new ArrayList<>(itemBuilder.getItemMeta().getLore());
            lore.add("§8§m---------------------------------");
            lore.add("§7Verkäufer: §e" + UUIDFetcher.getName(auctionHouseItem.getSeller()));
            lore.add("§7Preis: §e" + NumberFormat.getInstance(Locale.GERMAN).format(auctionHouseItem.getPrice()).replace(",", ".") + " Tokens");
            lore.add("§7Verbleibende Zeit: §e" + TimeUtil.beautifyTime(auctionHouseItem.getRemainingTime(), TimeUnit.MILLISECONDS, true, false));
            itemBuilder.lore(lore);
            setItem(slot, itemBuilder, event -> {
                //TODO: Buy event
            });
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

        setItem(47, new ItemBuilder(Material.HOPPER).glow().setName("§aItems filtern").lore(
                "§7Filtere die Items nach:",
                "§8- " + (filter == AuctionHouseItemFilter.PRICE_LOW_TO_HIGH ? "§aPreis (günstigste zuerst)" : "§7Preis (günstigste zuerst)"),
                "§8- " + (filter == AuctionHouseItemFilter.PRICE_HIGH_TO_LOW ? "§aPreis (teuerste zuerst)" : "§7Preis (teuerste zuerst)"),
                "§8- " + (filter == AuctionHouseItemFilter.EXPIRATION_DATE_LOW_TO_HIGH ? "§aAblaufdatum (früheste zuerst)" : "§7Ablaufdatum (früheste zuerst)"),
                "§8- " + (filter == AuctionHouseItemFilter.EXPIRATION_DATE_HIGH_TO_LOW ? "§aAblaufdatum (späteste zuerst)" : "§7Ablaufdatum (späteste zuerst)")
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
    }



    @Override
    public void close(InventoryCloseEvent event) {

    }
}
