package de.skymyth.auctionhouse.filter.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.auctionhouse.model.AuctionHouseItem;
import de.skymyth.auctionhouse.model.category.AuctionHouseItemCategory;

import java.util.List;

public class AuctionHouseItemCategoryFilter {

    SkyMythPlugin plugin;
    AuctionHouseItemCategory category;
    List<AuctionHouseItem> matches;

    private AuctionHouseItemCategoryFilter(SkyMythPlugin plugin, AuctionHouseItemCategory category) {
        this.plugin = plugin;
        this.category = category;
    }

    public static AuctionHouseItemCategoryFilter startSearching(SkyMythPlugin plugin, AuctionHouseItemCategory category) {
        return new AuctionHouseItemCategoryFilter(plugin, category);
    }

    public List<AuctionHouseItem> search() {
        List<AuctionHouseItem> allActiveItems = this.plugin.getAuctionHouseManager().getAuctionHouseItems();
        allActiveItems = allActiveItems.stream().filter(auctionHouseItem -> !auctionHouseItem.isExpired()).toList();
        allActiveItems = allActiveItems.stream().peek(auctionHouseItem -> {
            if (auctionHouseItem.getCategory() == null) {
                AuctionHouseItemCategory category = plugin.getAuctionHouseManager().determineCategory(auctionHouseItem);
                auctionHouseItem.setCategory(category);
            }
        }).toList();
        matches = allActiveItems.stream().filter(auctionHouseItem -> auctionHouseItem.getCategory().equals(category)).toList();
        return matches;
    }

}
