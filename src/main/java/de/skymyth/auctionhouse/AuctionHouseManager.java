package de.skymyth.auctionhouse;

import de.skymyth.SkyMythPlugin;
import de.skymyth.auctionhouse.model.AuctionHouseItem;
import de.skymyth.auctionhouse.model.category.AuctionHouseItemCategory;
import de.skymyth.auctionhouse.repository.AuctionHouseItemRepository;

import java.util.*;
import java.util.stream.Collectors;

public class AuctionHouseManager {

    SkyMythPlugin plugin;
    AuctionHouseItemRepository repository;
    Map<UUID, List<AuctionHouseItem>> auctionHouseItems;

    public AuctionHouseManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.repository = plugin.getMongoManager().create(AuctionHouseItemRepository.class);
        this.auctionHouseItems = new HashMap<>(this.repository.findAll().stream().collect(Collectors.groupingBy(AuctionHouseItem::getSeller)));
    }

    public void addAuctionHouseItem(AuctionHouseItem auctionHouseItem) {
        this.repository.save(auctionHouseItem);
        List<AuctionHouseItem> items = this.auctionHouseItems.getOrDefault(auctionHouseItem.getSeller(), new ArrayList<>(this.repository.findManyBySeller(auctionHouseItem.getSeller())));
        items.add(auctionHouseItem);
        this.auctionHouseItems.put(auctionHouseItem.getSeller(), items);
    }

    public void removeAuctionHouseItem(AuctionHouseItem auctionHouseItem) {
        this.repository.delete(auctionHouseItem);
        List<AuctionHouseItem> items = this.auctionHouseItems.getOrDefault(auctionHouseItem.getSeller(), new ArrayList<>(this.repository.findManyBySeller(auctionHouseItem.getSeller())));
        items.remove(auctionHouseItem);
        this.auctionHouseItems.put(auctionHouseItem.getSeller(), items);
    }

    public List<AuctionHouseItem> getExpiredAuctionHouseItems(UUID seller) {
        return this.auctionHouseItems.getOrDefault(seller, new ArrayList<>()).stream().filter(AuctionHouseItem::isExpired).collect(Collectors.toList());
    }

    public List<AuctionHouseItem> getAuctionHouseItems(UUID seller) {
        return this.auctionHouseItems.getOrDefault(seller, new ArrayList<>());
    }

    public List<AuctionHouseItem> getAllAuctionHouseItems(AuctionHouseItemCategory category) {
        return this.auctionHouseItems.values().stream().flatMap(Collection::stream).filter(item -> item.getCategory().equals(category)).collect(Collectors.toList());
    }

    public List<AuctionHouseItem> getAuctionHouseItems() {
        return this.auctionHouseItems.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

}
