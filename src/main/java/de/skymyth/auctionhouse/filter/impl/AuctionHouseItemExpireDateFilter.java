package de.skymyth.auctionhouse.filter.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.auctionhouse.filter.AuctionHouseItemFilter;
import de.skymyth.auctionhouse.filter.impl.result.AuctionHouseItemExpireDateFilterResult;
import de.skymyth.auctionhouse.model.AuctionHouseItem;

import java.util.ArrayList;
import java.util.List;

public class AuctionHouseItemExpireDateFilter {

    SkyMythPlugin plugin;
    AuctionHouseItemFilter searchingFor;
    List<AuctionHouseItem> matches;

    private AuctionHouseItemExpireDateFilter(SkyMythPlugin plugin, AuctionHouseItemFilter searchingFor) {
        this.plugin = plugin;
        this.searchingFor = searchingFor;
    }

    public static AuctionHouseItemExpireDateFilter startSearching(SkyMythPlugin plugin, AuctionHouseItemFilter searchingFor) {
        return new AuctionHouseItemExpireDateFilter(plugin, searchingFor);
    }

    public AuctionHouseItemExpireDateFilterResult search() {
        AuctionHouseItemFilter searchingFor = this.searchingFor;
        List<AuctionHouseItem> allActiveItems = this.plugin.getAuctionHouseManager().getAuctionHouseItems();
        allActiveItems = allActiveItems.stream().filter(auctionHouseItem -> !auctionHouseItem.isExpired()).toList();
        switch (searchingFor) {
            case EXPIRATION_DATE_LOW_TO_HIGH -> {
                matches = new ArrayList<>(allActiveItems);
                matches.sort((item1, item2) -> {
                    if (item1.getRemainingTime() == item2.getRemainingTime()) {
                        return 0;
                    }
                    return item1.getRemainingTime() < item2.getRemainingTime() ? -1 : 1;
                });
                return AuctionHouseItemExpireDateFilterResult.startFiltering(this, matches);
            }
            case EXPIRATION_DATE_HIGH_TO_LOW -> {
                matches = new ArrayList<>(allActiveItems);
                matches.sort((item1, item2) -> {
                    if (item1.getRemainingTime() == item2.getRemainingTime()) {
                        return 0;
                    }
                    return item1.getRemainingTime() > item2.getRemainingTime() ? -1 : 1;
                });
                return AuctionHouseItemExpireDateFilterResult.startFiltering(this, matches);
            }
            default -> throw new IllegalStateException("Unexpected value: " + searchingFor.getName());
        }
    }


}
