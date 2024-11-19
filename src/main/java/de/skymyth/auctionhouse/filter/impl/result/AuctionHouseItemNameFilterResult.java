package de.skymyth.auctionhouse.filter.impl.result;

import de.skymyth.auctionhouse.filter.impl.AuctionHouseItemNameFilter;
import de.skymyth.auctionhouse.model.AuctionHouseItem;
import lombok.Getter;

import java.util.List;

public class AuctionHouseItemNameFilterResult {

    AuctionHouseItemNameFilter filter;
    @Getter
    List<AuctionHouseItem> matches;

    private AuctionHouseItemNameFilterResult(AuctionHouseItemNameFilter filter, List<AuctionHouseItem> matches) {
        this.filter = filter;
        this.matches = matches;
    }

    public static AuctionHouseItemNameFilterResult startFiltering(AuctionHouseItemNameFilter filter, List<AuctionHouseItem> matches) {
        return new AuctionHouseItemNameFilterResult(filter, matches);
    }

}
