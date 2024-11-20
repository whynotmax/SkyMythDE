package de.skymyth.auctionhouse.model;

import de.skymyth.auctionhouse.model.category.AuctionHouseItemCategory;
import eu.koboo.en2do.repository.entity.Id;
import eu.koboo.en2do.repository.entity.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuctionHouseItem {

    @Id
    int id;

    UUID seller;
    ItemStack itemStack;

    boolean expired;

    long price;
    AuctionHouseItemCategory category;

    long start;
    Duration duration;

    @Transient
    public boolean isExpired() {
        return System.currentTimeMillis() > start + duration.toMillis();
    }

    @Transient
    public long getRemainingTime() {
        return (start + duration.toMillis() - System.currentTimeMillis()) / 1000;
    }

    @Transient
    public boolean getExpired() {
        return expired;
    }

}
