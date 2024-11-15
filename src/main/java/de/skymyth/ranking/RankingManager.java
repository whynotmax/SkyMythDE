package de.skymyth.ranking;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.UserManager;
import de.skymyth.user.model.User;
import de.skymyth.utility.Util;
import de.skymyth.utility.item.ItemBuilder;
import de.skymyth.utility.item.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RankingManager {

    SkyMythPlugin plugin;
    World world = Bukkit.getWorld("Spawn");
    List<ArmorStand> rankingStands;

    public RankingManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.rankingStands = new ArrayList<>();

        ArmorStand ranking1 = this.world.spawn(plugin.getLocationManager().getPosition("ranking-1").getLocation(), ArmorStand.class);
        ArmorStand ranking2 = this.world.spawn(plugin.getLocationManager().getPosition("ranking-2").getLocation(), ArmorStand.class);
        ArmorStand ranking3 = this.world.spawn(plugin.getLocationManager().getPosition("ranking-3").getLocation(), ArmorStand.class);

        ArmorStand[] armorStands = new ArmorStand[]{ranking1, ranking2, ranking3};


        for (ArmorStand armorStand : armorStands) {
            armorStand.setSmall(true);
            armorStand.setCustomNameVisible(true);
            armorStand.setHelmet(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDEzZTk2ZGY2ZWQ0YjcwYTVhNzBmYzI5ZGNkZTkzMTRkYmU5NzY2OTY0NzRmMTIwZTBiMzBlYTVkN2I5NmIzYSJ9fX0="));
            this.rankingStands.add(armorStand);
        }

        Map<UUID, Long> killsMap = new HashMap<>();

        for (User user : plugin.getUserManager().getRepository().findAll()) {
            killsMap.put(user.getUniqueId(), user.getBalance());
        }

        AtomicInteger integer = new AtomicInteger(1);
        Util.sortMapByValue(killsMap).forEach((uuid, kills) -> {

            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            if (integer.get() == 1) {
                ranking1.setCustomName("§7" + player.getName());
                ranking1.setHelmet(null);
                ranking1.setHelmet(new ItemBuilder(Material.SKULL_ITEM).skullOwner(player.getName()));
            }
            if (integer.get() == 2) {
                ranking2.setCustomName("§7" + player.getName());
                ranking2.setHelmet(null);
                ranking2.setHelmet(new ItemBuilder(Material.SKULL_ITEM).skullOwner(player.getName()));
            }
            if (integer.get() == 3) {
                ranking3.setCustomName("§7" + player.getName());
                ranking3.setHelmet(null);
                ranking3.setHelmet(new ItemBuilder(Material.SKULL_ITEM).skullOwner(player.getName()));
            }

            System.out.println(player.getName() + ";" + kills + ";" + integer.get());
            if (integer.get() > 3) return;
            integer.getAndIncrement();
        });
    }

    public void delete() {
        for (ArmorStand rankingStand : this.rankingStands) {
            rankingStand.setHealth(0);
            rankingStand.remove();
        }
    }
}
