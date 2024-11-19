package de.skymyth.auctionhouse.filter.impl.result;

import de.skymyth.auctionhouse.filter.impl.AuctionHouseItemExpireDateFilter;
import de.skymyth.auctionhouse.model.AuctionHouseItem;
import lombok.Getter;

import java.util.List;

public class AuctionHouseItemExpireDateFilterResult {

    AuctionHouseItemExpireDateFilter filter;
    @Getter
    List<AuctionHouseItem> matches;

    private AuctionHouseItemExpireDateFilterResult(AuctionHouseItemExpireDateFilter filter, List<AuctionHouseItem> matches) {
        this.filter = filter;
        this.matches = matches;
    }

    public static AuctionHouseItemExpireDateFilterResult startFiltering(AuctionHouseItemExpireDateFilter filter, List<AuctionHouseItem> matches) {
        return new AuctionHouseItemExpireDateFilterResult(filter, matches);
    }

}
