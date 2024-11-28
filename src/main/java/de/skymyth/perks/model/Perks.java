package de.skymyth.perks.model;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.function.Consumer;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum Perks {

    NO_HUNGER("Kein Hunger", Material.COOKED_BEEF, "Werde nie wieder hungrig!", 1_000_000, Duration.ofHours(3), user -> {
        Player player = Bukkit.getPlayer(user.getUniqueId());
        if (player != null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Perk §eKein Hunger §7wurde deaktiviert.");
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du wirst nun wieder hungrig.");
            player.setFoodLevel(20);
        }
    }),
    STRENTH("Stärke", Material.DIAMOND_SWORD, "Erhalte Stärke 2!", 250_000, Duration.ofMinutes(90), user -> {
        Player player = Bukkit.getPlayer(user.getUniqueId());
        if (player != null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Perk §eStärke §7wurde deaktiviert.");
            player.removePotionEffect(org.bukkit.potion.PotionEffectType.INCREASE_DAMAGE);
        }
    }),
    SPEED("Speed", Material.SUGAR, "Erhalte Speed 2!", 250_000, Duration.ofMinutes(90), user -> {
        Player player = Bukkit.getPlayer(user.getUniqueId());
        if (player != null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Perk §eSpeed §7wurde deaktiviert.");
            player.removePotionEffect(org.bukkit.potion.PotionEffectType.SPEED);
        }
    }),
    JUMP_BOOST("Sprungkraft", Material.FEATHER, "Erhalte Jump Boost 2!", 250_000, Duration.ofMinutes(90), user -> {
        Player player = Bukkit.getPlayer(user.getUniqueId());
        if (player != null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Perk §eSprungkraft §7wurde deaktiviert.");
            player.removePotionEffect(org.bukkit.potion.PotionEffectType.JUMP);
        }
    }),
    INVISIBILITY("Unsichtbarkeit", Material.GOLDEN_CARROT, "Erhalte Unsichtbarkeit!", -1, Duration.ofMinutes(3), user -> {
        Player player = Bukkit.getPlayer(user.getUniqueId());
        if (player != null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Perk §eUnsichtbarkeit §7wurde deaktiviert.");
            player.removePotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY);
        }
    }),
    FLY("Fliegen", Material.FEATHER, "Erhalte die Fähigkeit zu fliegen!", 200_000, Duration.ofMinutes(90), user -> {
        Player player = Bukkit.getPlayer(user.getUniqueId());
        if (player != null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Perk §eFliegen §7wurde deaktiviert.");
            if (player.isOp()) return;
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }),
    DOUBLE_JUMP("Doppelsprung", Material.GOLD_BOOTS, "Erhalte die Fähigkeit, doppelt zu springen!", 200_000, Duration.ofMinutes(90), user -> {
        Player player = Bukkit.getPlayer(user.getUniqueId());
        if (player != null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Perk §eDoppelsprung §7wurde deaktiviert.");
        }
    }),
    ;

    public static final Perks[] VALUES = values();
    String name;
    Material displayItem;
    String description;
    long price;
    Duration durationPerPrice;
    Consumer<User> removalAction;


}
