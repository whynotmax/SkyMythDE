package de.skymyth.perks.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;

import java.time.Duration;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum Perks {

    NO_HUNGER("Kein Hunger", Material.COOKED_BEEF, "Werde nie wieder hungrig!", 1_000_000, Duration.ofHours(3)),
    STRENTH("Stärke", Material.DIAMOND_SWORD, "Erhalte Stärke 2!", 250_000, Duration.ofMinutes(90)),
    SPEED("Speed", Material.SUGAR, "Erhalte Speed 2!", 250_000, Duration.ofMinutes(90)),
    JUMP_BOOST("Sprungkraft", Material.FEATHER, "Erhalte Jump Boost 2!", 250_000, Duration.ofMinutes(90)),
    INVISIBILITY("Unsichtbarkeit", Material.GOLDEN_CARROT, "Erhalte Unsichtbarkeit!", -1, Duration.ofMinutes(3)),
    FLY("Fliegen", Material.FEATHER, "Erhalte die Fähigkeit zu fliegen!", 200_000, Duration.ofMinutes(90)),
    DOUBLE_JUMP("Doppelsprung", Material.GOLD_BOOTS, "Erhalte die Fähigkeit, doppelt zu springen!", 200_000, Duration.ofMinutes(90)),
    ;

    String name;
    Material displayItem;
    String description;
    long price;
    Duration durationPerPrice;

    public static final Perks[] VALUES = values();


}
