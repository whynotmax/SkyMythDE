package de.skymyth.auctionhouse.filter.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.auctionhouse.filter.impl.result.AuctionHouseItemNameFilterResult;
import de.skymyth.auctionhouse.model.AuctionHouseItem;

import java.util.ArrayList;
import java.util.List;

public class AuctionHouseItemNameFilter {

    SkyMythPlugin plugin;
    String searchingFor;
    List<AuctionHouseItem> matches;

    private AuctionHouseItemNameFilter(SkyMythPlugin plugin, String searchingFor) {
        this.plugin = plugin;
        this.searchingFor = searchingFor;
    }

    public static AuctionHouseItemNameFilter startSearching(SkyMythPlugin plugin, String searchingFor) {
        return new AuctionHouseItemNameFilter(plugin, searchingFor);
    }

    public AuctionHouseItemNameFilterResult search() {
        String searchingFor = this.searchingFor;
        List<AuctionHouseItem> matches = this.matches == null ? null : this.matches;
        List<AuctionHouseItem> allActiveItems = this.plugin.getAuctionHouseManager().getAuctionHouseItems();
        allActiveItems = allActiveItems.stream().filter(auctionHouseItem -> !auctionHouseItem.isExpired()).toList();
        if (searchingFor == null || searchingFor.isEmpty()) {
            this.matches = allActiveItems;
            return AuctionHouseItemNameFilterResult.startFiltering(this, matches);
        }
        matches = new ArrayList<>(allActiveItems.stream().filter(auctionHouseItem -> {
            if (!auctionHouseItem.getItemStack().hasItemMeta()) {
                return false;
            }
            if (!auctionHouseItem.getItemStack().getItemMeta().hasDisplayName()) {
                //Search for material name
                return auctionHouseItem.getItemStack().getType().name().toLowerCase().contains(searchingFor.toLowerCase());
            }
            String itemName = auctionHouseItem.getItemStack().getItemMeta().getDisplayName();
            return itemName.toLowerCase().contains(searchingFor.toLowerCase());
        }).toList());

        return AuctionHouseItemNameFilterResult.startFiltering(this, matches);
    }



}
