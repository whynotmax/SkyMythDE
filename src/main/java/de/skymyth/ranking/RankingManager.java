package de.skymyth.ranking;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;
import de.skymyth.utility.Util;
import de.skymyth.utility.item.ItemBuilder;
import de.skymyth.utility.item.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RankingManager {

    SkyMythPlugin plugin;
    World world = Bukkit.getWorld("Spawn");
    List<ArmorStand> rankingStands;

    public RankingManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.rankingStands = new ArrayList<>();

        for (Entity entity : this.world.getEntities()) {
            if(entity.getType() != EntityType.ARMOR_STAND) continue;
            if(entity.getLocation().distance(plugin.getLocationManager().getPosition("ranking-1").getLocation()) < 10) {
                entity.remove();
            }
        }

        ArmorStand ranking1 = this.world.spawn(plugin.getLocationManager().getPosition("ranking-1")
                .getLocation().subtract(1, 0, 0), ArmorStand.class);
        ArmorStand ranking2 = this.world.spawn(plugin.getLocationManager().getPosition("ranking-2")
                .getLocation().subtract(1, 0, 0), ArmorStand.class);
        ArmorStand ranking3 = this.world.spawn(plugin.getLocationManager().getPosition("ranking-3")
                .getLocation().subtract(1, 0, 0), ArmorStand.class);

        ArmorStand[] armorStands = new ArmorStand[]{ranking1, ranking2, ranking3};



        for (ArmorStand armorStand : armorStands) {
            armorStand.setSmall(true);
            armorStand.setGravity(false);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName("§7Nicht besetzt");
            armorStand.setBasePlate(false);
            armorStand.setArms(true);
            //armorStand.setRightArmPose(new EulerAngle(56F,305F,0F));
            //armorStand.setLeftArmPose(new EulerAngle(18F,0F,0F));
            //armorStand.setLeftLegPose(new EulerAngle(42F, 328F, 0F));
            //armorStand.setRightLegPose(new EulerAngle(38F, 312F, 40F));
            armorStand.setHelmet(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDEzZTk2ZGY2ZWQ0YjcwYTVhNzBmYzI5ZGNkZTkzMTRkYmU5NzY2OTY0NzRmMTIwZTBiMzBlYTVkN2I5NmIzYSJ9fX0="));
            this.rankingStands.add(armorStand);
        }

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Map<UUID, Long> killsMap = new HashMap<>();

            for (User user : plugin.getUserManager().getRepository().findAll()) {
                killsMap.put(user.getUniqueId(), user.getKills());
            }

            AtomicInteger integer = new AtomicInteger(1);
            for (Map.Entry<UUID, Long> entry : Util.sortMapByValue(killsMap).entrySet()) {
                UUID uuid = entry.getKey();
                Long kills = entry.getValue();

                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

                if(kills < 1) continue;
                if(player == null) continue;


                if (integer.get() == 1) {
                    ranking1.setCustomName("§8#§c1 §7" + player.getName() + " §8( §a" + kills + " §7Kills §8)");
                    ranking1.setHelmet(new ItemBuilder(Material.SKULL_ITEM).skullOwner(player.getName()));
                    ranking1.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                    ranking1.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                    ranking1.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
                    ranking1.setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                }
                if (integer.get() == 2) {
                    ranking2.setCustomName("§8#§c2 §7" + player.getName() + " §8( §a" + kills + " §7Kills §8)");
                    ranking2.setHelmet(new ItemBuilder(Material.SKULL_ITEM).skullOwner(player.getName()));
                    ranking2.setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
                    ranking2.setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
                    ranking2.setBoots(new ItemStack(Material.GOLD_BOOTS));
                    ranking2.setItemInHand(new ItemStack(Material.BOW));
                }
                if (integer.get() == 3) {
                    ranking3.setCustomName("§8#§c3 §7" + player.getName() + " §8( §a" + kills + " §7Kills §8)");
                    ranking3.setHelmet(new ItemBuilder(Material.SKULL_ITEM).skullOwner(player.getName()));
                    ranking3.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                    ranking3.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                    ranking3.setBoots(new ItemStack(Material.LEATHER_BOOTS));
                    ranking3.setItemInHand(new ItemStack(Material.ENDER_PEARL));
                }

                System.out.println(player.getName() + ";" + kills + ";" + integer.get());
                if (integer.get() > 3) continue;
                integer.getAndIncrement();
            }
        },20L, 20*60*1);
    }

    public void delete() {
        for (ArmorStand rankingStand : this.rankingStands) {
            rankingStand.setHealth(0);
            rankingStand.remove();
        }
    }
}
