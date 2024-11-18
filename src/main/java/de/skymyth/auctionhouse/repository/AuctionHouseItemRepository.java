package de.skymyth.auctionhouse.repository;

import de.skymyth.auctionhouse.model.AuctionHouseItem;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

import java.util.List;
import java.util.UUID;

@Collection("auctionhouse_items")
public interface AuctionHouseItemRepository extends Repository<AuctionHouseItem, Integer> {

    List<AuctionHouseItem> findManyBySeller(UUID seller);

}
