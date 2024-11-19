package de.skymyth.auctionhouse.filter.impl.result;

import de.skymyth.auctionhouse.filter.impl.AuctionHouseItemPriceFilter;
import de.skymyth.auctionhouse.model.AuctionHouseItem;
import lombok.Getter;

import java.util.List;

public class AuctionHouseItemPriceFilterResult {

    AuctionHouseItemPriceFilter filter;
    @Getter
    List<AuctionHouseItem> matches;

    private AuctionHouseItemPriceFilterResult(AuctionHouseItemPriceFilter filter, List<AuctionHouseItem> matches) {
        this.filter = filter;
        this.matches = matches;
    }

    public static AuctionHouseItemPriceFilterResult startFiltering(AuctionHouseItemPriceFilter filter, List<AuctionHouseItem> matches) {
        return new AuctionHouseItemPriceFilterResult(filter, matches);
    }

}
