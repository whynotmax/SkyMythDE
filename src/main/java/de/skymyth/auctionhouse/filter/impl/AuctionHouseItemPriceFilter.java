package de.skymyth.auctionhouse.filter.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.auctionhouse.filter.AuctionHouseItemFilter;
import de.skymyth.auctionhouse.filter.impl.result.AuctionHouseItemPriceFilterResult;
import de.skymyth.auctionhouse.model.AuctionHouseItem;

import java.util.ArrayList;
import java.util.List;

public class AuctionHouseItemPriceFilter {

    SkyMythPlugin plugin;
    AuctionHouseItemFilter searchingFor;
    List<AuctionHouseItem> matches;

    private AuctionHouseItemPriceFilter(SkyMythPlugin plugin, AuctionHouseItemFilter searchingFor) {
        this.plugin = plugin;
        this.searchingFor = searchingFor;
    }

    public static AuctionHouseItemPriceFilter startSearching(SkyMythPlugin plugin, AuctionHouseItemFilter searchingFor) {
        return new AuctionHouseItemPriceFilter(plugin, searchingFor);
    }

    public AuctionHouseItemPriceFilterResult search() {
        AuctionHouseItemFilter searchingFor = this.searchingFor;
        List<AuctionHouseItem> allActiveItems = this.plugin.getAuctionHouseManager().getAuctionHouseItems();
        allActiveItems = allActiveItems.stream().filter(auctionHouseItem -> !auctionHouseItem.isExpired()).toList();
        switch (searchingFor) {
            case PRICE_HIGH_TO_LOW -> {
                matches = new ArrayList<>(allActiveItems);
                matches.sort((item1, item2) -> {
                    if (item1.getPrice() == item2.getPrice()) {
                        return 0;
                    }
                    return item1.getPrice() > item2.getPrice() ? -1 : 1;
                });
                return AuctionHouseItemPriceFilterResult.startFiltering(this, matches);
            }
            case PRICE_LOW_TO_HIGH -> {
                matches = new ArrayList<>(allActiveItems);
                matches.sort((item1, item2) -> {
                    if (item1.getPrice() == item2.getPrice()) {
                        return 0;
                    }
                    return item1.getPrice() < item2.getPrice() ? -1 : 1;
                });
                return AuctionHouseItemPriceFilterResult.startFiltering(this, matches);
            }
            default -> throw new IllegalStateException("Unexpected value: " + searchingFor.getName());
        }
    }



}
