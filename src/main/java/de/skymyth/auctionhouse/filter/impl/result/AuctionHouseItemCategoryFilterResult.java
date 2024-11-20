package de.skymyth.auctionhouse.filter.impl.result;

import de.skymyth.auctionhouse.filter.impl.AuctionHouseItemCategoryFilter;
import de.skymyth.auctionhouse.filter.impl.AuctionHouseItemExpireDateFilter;
import de.skymyth.auctionhouse.model.AuctionHouseItem;
import de.skymyth.auctionhouse.model.category.AuctionHouseItemCategory;
import lombok.Getter;

import java.util.List;

public class AuctionHouseItemCategoryFilterResult {

    AuctionHouseItemCategoryFilter filter;
    @Getter
    List<AuctionHouseItem> matches;

    private AuctionHouseItemCategoryFilterResult(AuctionHouseItemCategoryFilter filter, List<AuctionHouseItem> matches) {
        this.filter = filter;
        this.matches = matches;
    }

    public static AuctionHouseItemCategoryFilterResult startFiltering(AuctionHouseItemCategoryFilter filter, List<AuctionHouseItem> matches) {
        return new AuctionHouseItemCategoryFilterResult(filter, matches);
    }

}
