package de.skymyth.auctionhouse.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum AuctionHouseItemFilter {

    PRICE_LOW_TO_HIGH("Preis (günstigste zuerst)"),
    PRICE_HIGH_TO_LOW("Preis (teuerste zuerst)"),
    EXPIRATION_DATE_LOW_TO_HIGH("Ablaufdatum (früheste zuerst)"),
    EXPIRATION_DATE_HIGH_TO_LOW("Ablaufdatum (späteste zuerst)")
    ;

    String name;

}
