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
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;
import java.util.ArrayList;
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

        for (ItemStack item : this.items) {
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
    }

    //TODO:
    public List<ItemStack> replaceName(List<ItemStack> items) {
        List<ItemStack> newItems = new ArrayList<>();
        for (ItemStack item : items) {
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                ItemStack clone = item.clone();
                ItemMeta cloneMeta = clone.getItemMeta();
                cloneMeta.setDisplayName("§8» §7Kit §8┃ §e" + this.getName());
                clone.setItemMeta(cloneMeta);
                newItems.add(clone);
            }
        }
        return newItems;
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

        for (ItemStack item : this.items) {
            if (item.getType().name().toUpperCase().contains("HELMET")) {
                if (player.getInventory().getHelmet() == null || player.getInventory().getHelmet().getType() == Material.AIR) {
                    player.getInventory().setHelmet(item);
                } else {
                    player.getInventory().addItem(item);
                }
                continue;
            } else if (item.getType().name().toUpperCase().contains("CHESTPLATE")) {
                if (player.getInventory().getChestplate() == null || player.getInventory().getChestplate().getType() == Material.AIR) {
                    player.getInventory().setChestplate(item);
                } else {
                    player.getInventory().addItem(item);
                }
                continue;
            } else if (item.getType().name().toUpperCase().contains("LEGGINGS")) {
                if (player.getInventory().getLeggings() == null || player.getInventory().getLeggings().getType() == Material.AIR) {
                    player.getInventory().setLeggings(item);
                } else {
                    player.getInventory().addItem(item);
                }
                continue;
            } else if (item.getType().name().toUpperCase().contains("BOOTS")) {
                if (player.getInventory().getBoots() == null || player.getInventory().getBoots().getType() == Material.AIR) {
                    player.getInventory().setBoots(item);
                } else {
                    player.getInventory().addItem(item);
                }
                continue;
            }
            player.getInventory().addItem(item);
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
