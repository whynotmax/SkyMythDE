package de.skymyth.kit.model;

import de.skymyth.SkyMythPlugin;
import de.skymyth.kit.model.type.KitType;
import de.skymyth.user.model.User;
import de.skymyth.user.model.cooldown.Cooldown;
import de.skymyth.utility.TimeUtil;
import eu.koboo.en2do.repository.entity.Id;
import eu.koboo.en2do.repository.entity.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Kit {

    @Id
    String name;
    Material displayItem;

    KitType type;
    boolean enabled;

    List<UUID> oneTimeUsers;

    String permission; //ONLY IF it's not a oneTime kit AND not a buyable kit (e.g. rank specific kits)

    long price;
    Duration cooldown;

    List<ItemStack> items;

    @Transient
    public void giveTo(User user) {
        Player player = Bukkit.getPlayer(user.getUniqueId());
        if (player == null) {
            return;
        }

        if (user.hasCooldown("kit_" + name.toLowerCase())) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du musst noch §e" + TimeUtil.beautifyTime(user.getCooldown("kit_" + name.toLowerCase()).getRemainingTime(), TimeUnit.MILLISECONDS, true, true) + " §7warten, bevor du das Kit §e" + name + " §7erhalten kannst.");
            return;
        }

        for (ItemStack item : items) {
            if (item.getType().name().contains("HELMET")) {
                if (player.getInventory().getHelmet() != null) {
                    player.getInventory().addItem(player.getInventory().getHelmet());
                } else {
                    player.getInventory().addItem(item);
                }
                continue;
            } else if (item.getType().name().contains("CHESTPLATE")) {
                if (player.getInventory().getChestplate() != null) {
                    player.getInventory().addItem(player.getInventory().getChestplate());
                } else {
                    player.getInventory().addItem(item);
                }
                continue;
            } else if (item.getType().name().contains("LEGGINGS")) {
                if (player.getInventory().getLeggings() != null) {
                    player.getInventory().addItem(player.getInventory().getLeggings());
                } else {
                    player.getInventory().addItem(item);
                }
                continue;
            } else if (item.getType().name().contains("BOOTS")) {
                if (player.getInventory().getBoots() != null) {
                    player.getInventory().addItem(player.getInventory().getBoots());
                } else {
                    player.getInventory().addItem(item);
                }
                continue;
            }
            player.getInventory().addItem(item);
        }
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast das Kit §e" + name + " §7erhalten.");

        Cooldown cooldown = new Cooldown();
        cooldown.setName("kit_" + name.toLowerCase());
        cooldown.setDuration(this.cooldown);
        cooldown.start();
        user.addCooldown(cooldown);
    }

}
