package de.skymyth.kit.model;

import de.skymyth.SkyMythPlugin;
import de.skymyth.kit.model.type.KitType;
import de.skymyth.user.model.User;
import de.skymyth.user.model.cooldown.Cooldown;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.item.ItemBuilder;
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
    boolean systemOnly;

    List<UUID> oneTimeUsers;

    String permission; //ONLY IF it's not a oneTime kit AND not a buyable kit (e.g. rank specific kits)

    long price;
    Duration cooldown;

    List<ItemStack> items;

    @Transient
    public void giveToAsVoucher(User user) {
        Player player = Bukkit.getPlayer(user.getUniqueId());
        if (player == null) {
            return;
        }

        for (ItemBuilder item : this.items.stream().map(ItemStack::clone).map(ItemBuilder::new).toList()) {
            if (item.getType().name().contains("HELMET")) {
                if (player.getInventory().getHelmet() != null) {
                    player.getInventory().setHelmet(item.setName("§8» §7Kit §8┃ §e" + name));
                } else {
                    player.getInventory().addItem(item.setName("§8» §7Kit §8┃ §e" + name));
                }
                continue;
            } else if (item.getType().name().contains("CHESTPLATE")) {
                if (player.getInventory().getChestplate() != null) {
                    player.getInventory().setChestplate(item.setName("§8» §7Kit §8┃ §e" + name));
                } else {
                    player.getInventory().addItem(item.setName("§8» §7Kit §8┃ §e" + name));
                }
                continue;
            } else if (item.getType().name().contains("LEGGINGS")) {
                if (player.getInventory().getLeggings() != null) {
                    player.getInventory().setLeggings(item.setName("§8» §7Kit §8┃ §e" + name));
                } else {
                    player.getInventory().addItem(item.setName("§8» §7Kit §8┃ §e" + name));
                }
                continue;
            } else if (item.getType().name().contains("BOOTS")) {
                if (player.getInventory().getBoots() != null) {
                    player.getInventory().setBoots(item.setName("§8» §7Kit §8┃ §e" + name));
                } else {
                    player.getInventory().addItem(item.setName("§8» §7Kit §8┃ §e" + name));
                }
                continue;
            }
            player.getInventory().addItem(item);
        }
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast das Kit §e" + name + " §7erhalten.");
    }

    @Transient
    public void giveTo(User user, SkyMythPlugin plugin) {
        Player player = Bukkit.getPlayer(user.getUniqueId());
        if (player == null) {
            return;
        }

        if (user.isOnCooldown("kit" + name.toLowerCase())) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du musst noch §e" + TimeUtil.beautifyTime(user.getCooldown("kit" + name.toLowerCase()).getRemainingTime(), TimeUnit.MILLISECONDS, true, true) + " §7warten, bevor du das Kit §e" + name + " §7abholen kannst.");
            return;
        }

        for (ItemBuilder item : this.items.stream().map(ItemStack::clone).map(ItemBuilder::new).toList()) {
            if (item.getType().name().toUpperCase().contains("HELMET")) {
                if (player.getInventory().getHelmet() == null || player.getInventory().getHelmet().getType() == Material.AIR) {
                    player.getInventory().setHelmet(item.setName("§8» §7Kit §8┃ §e" + name));
                } else {
                    player.getInventory().addItem(item.setName("§8» §7Kit §8┃ §e" + name));
                }
                continue;
            } else if (item.getType().name().toUpperCase().contains("CHESTPLATE")) {
                if (player.getInventory().getChestplate() == null || player.getInventory().getChestplate().getType() == Material.AIR) {
                    player.getInventory().setChestplate(item.setName("§8» §7Kit §8┃ §e" + name));
                } else {
                    player.getInventory().addItem(item.setName("§8» §7Kit §8┃ §e" + name));
                }
                continue;
            } else if (item.getType().name().toUpperCase().contains("LEGGINGS")) {
                if (player.getInventory().getLeggings() == null || player.getInventory().getLeggings().getType() == Material.AIR) {
                    player.getInventory().setLeggings(item.setName("§8» §7Kit §8┃ §e" + name));
                } else {
                    player.getInventory().addItem(item.setName("§8» §7Kit §8┃ §e" + name));
                }
                continue;
            } else if (item.getType().name().toUpperCase().contains("BOOTS")) {
                if (player.getInventory().getBoots() == null || player.getInventory().getBoots().getType() == Material.AIR) {
                    player.getInventory().setBoots(item.setName("§8» §7Kit §8┃ §e" + name));
                } else {
                    player.getInventory().addItem(item.setName("§8» §7Kit §8┃ §e" + name));
                }
                continue;
            }
            player.getInventory().addItem(item.setName("§8» §7Kit §8┃ §e" + name));
        }
        player.updateInventory();
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast das Kit §e" + name + " §7erhalten.");

        if (this.cooldown != null) {
            Cooldown cooldown = new Cooldown();
            cooldown.setName("kit" + name.toLowerCase());
            cooldown.setDuration(Duration.ofMillis(this.cooldown.toMillis()));
            cooldown.start();
            user.addCooldown(cooldown);
            plugin.getUserManager().saveUser(user);
        }
    }

}
