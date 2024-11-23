package de.skymyth.ranking;

import de.skymyth.SkyMythPlugin;
import de.skymyth.pvp.model.PvPRank;
import de.skymyth.user.model.User;
import de.skymyth.utility.UUIDFetcher;
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RankingManager implements Listener {

    SkyMythPlugin plugin;
    World world = Bukkit.getWorld("Spawn");
    List<ArmorStand> rankingStands;

    public RankingManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.rankingStands = new ArrayList<>();

        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getScheduler().runTaskTimer(plugin, this::update, 20L, 20 * 60 * 5);
    }

    public void update() {
        for (Entity entity : this.world.getEntities()) {
            if (entity.getType() != EntityType.ARMOR_STAND) continue;
            if (entity.getLocation().distance(plugin.getLocationManager().getPosition("ranking-1").getLocation()) < 10) {
                entity.remove();
            }
        }

        ArmorStand ranking1 = this.world.spawn(plugin.getLocationManager().getPosition("ranking-1")
                .getLocation().clone().subtract(0, 0, 0.5), ArmorStand.class);
        ArmorStand ranking2 = this.world.spawn(plugin.getLocationManager().getPosition("ranking-2")
                .getLocation().clone().subtract(0, 0, 0.5), ArmorStand.class);
        ArmorStand ranking3 = this.world.spawn(plugin.getLocationManager().getPosition("ranking-3")
                .getLocation().clone().subtract(0, 0, 0.5), ArmorStand.class);

        ArmorStand[] armorStands = new ArmorStand[]{ranking1, ranking2, ranking3};


        for (ArmorStand armorStand : armorStands) {
            armorStand.setSmall(true);
            armorStand.setGravity(false);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName("§7Nicht besetzt");
            armorStand.setBasePlate(false);
            armorStand.setArms(true);
            armorStand.setHelmet(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDEzZTk2ZGY2ZWQ0YjcwYTVhNzBmYzI5ZGNkZTkzMTRkYmU5NzY2OTY0NzRmMTIwZTBiMzBlYTVkN2I5NmIzYSJ9fX0="));
            this.rankingStands.add(armorStand);
        }

        Map<UUID, Long> killsMap = new HashMap<>();

        for (User user : plugin.getUserManager().getRepository().findAll()) {
            if (user.getKills() < 1) continue;
            killsMap.put(user.getUniqueId(), user.getKills());
        }

        AtomicInteger integer = new AtomicInteger(1);
        for (Map.Entry<UUID, Long> entry : Util.sortMapByValue(killsMap).entrySet()) {
            UUID uuid = entry.getKey();
            long kills = entry.getValue();

            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            String playerName = UUIDFetcher.getName(uuid);
            User user = plugin.getUserManager().getUser(uuid);

            if (kills < 1) continue;
            if (player == null) continue;

            String killsText = (kills > 1 ? "§8( §a%s Kills §8)" : "§8( §a %s Kill §8)");

            if (integer.get() == 1) {
                ranking1.setCustomName("§8#§51 §7" + playerName + String.format(killsText, kills));
                ranking1.setHelmet(plugin.getSkullLoader().getSkull(uuid));
                ranking1.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                ranking1.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                ranking1.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
                ranking1.setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
            }
            if (integer.get() == 2) {
                ranking2.setCustomName("§8#§52 §7" + playerName + String.format(killsText, kills));
                ranking1.setHelmet(plugin.getSkullLoader().getSkull(uuid));
                ranking2.setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
                ranking2.setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
                ranking2.setBoots(new ItemStack(Material.GOLD_BOOTS));
                ranking2.setItemInHand(new ItemStack(Material.BOW));
            }
            if (integer.get() == 3) {
                ranking3.setCustomName("§8#§53 §7" + playerName + String.format(killsText, kills));
                ranking1.setHelmet(plugin.getSkullLoader().getSkull(uuid));
                ranking3.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                ranking3.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                ranking3.setBoots(new ItemStack(Material.LEATHER_BOOTS));
                ranking3.setItemInHand(new ItemStack(Material.ENDER_PEARL));
            }
            if (integer.get() > 3) continue;
            integer.getAndIncrement();
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ArmorStand armorStand) {
            if (rankingStands.contains(armorStand)) {
                event.setCancelled(true);
            }
        }
    }

    public void delete() {
        for (ArmorStand rankingStand : this.rankingStands) {
            rankingStand.setHealth(0);
            rankingStand.remove();
        }
    }
}
