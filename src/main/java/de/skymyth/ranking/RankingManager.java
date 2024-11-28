package de.skymyth.ranking;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.TitleUtil;
import de.skymyth.utility.UUIDFetcher;
import de.skymyth.utility.Util;
import de.skymyth.utility.item.SkullCreator;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class RankingManager implements Listener {

    SkyMythPlugin plugin;
    World world = Bukkit.getWorld("Spawn");
    List<ArmorStand> rankingStands;

    @Getter
    @AllArgsConstructor
    public enum RankingType {
        TOKENS("Tokens"),
        KILLS("Kills"),
        DEATHS("Tode"),
        TROPHIES( "Trophäen");

        String displayName;
    }

    public RankingManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.rankingStands = new ArrayList<>();

        Bukkit.getPluginManager().registerEvents(this, plugin);
        AtomicReference<RankingType> currentState = new AtomicReference<>(RankingType.KILLS);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            RankingType nextState;
            if (currentState.get() == RankingType.KILLS) {
                nextState = RankingType.TOKENS;
            } else if (currentState.get() == RankingType.TOKENS) {
                nextState = RankingType.DEATHS;
            } else if (currentState.get() == RankingType.DEATHS) {
                nextState = RankingType.TROPHIES;
            } else {
                nextState = RankingType.KILLS;
            }
            currentState.set(nextState);
            for (Player spawn : Bukkit.getWorld("Spawn").getPlayers()) {
                TitleUtil.sendActionBar(spawn, SkyMythPlugin.PREFIX + "§7Ranking wurde zu §e" + nextState.getDisplayName() + " §7geändert!");
            }

            this.update(currentState.get(), new Location[]{
                    plugin.getLocationManager().getPosition("ranking-1").getLocation(),
                    plugin.getLocationManager().getPosition("ranking-2").getLocation(),
                    plugin.getLocationManager().getPosition("ranking-3").getLocation()
            });
        }, 20L, 20 * 60 * 3);

        AtomicInteger ticks = new AtomicInteger(180);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Hologram hologram = DHAPI.getHologram("ranking");
            if (hologram != null) {
                int currentTicks = ticks.getAndDecrement();

                if (currentTicks <= 1) {
                    ticks.set(180);
                    return;
                }

                DHAPI.setHologramLine(
                        hologram,
                        1,
                        "§7Nächstes Update in: §e" + TimeUtil.beautifyTime(
                                currentTicks * 1000L,
                                TimeUnit.MILLISECONDS,
                                true,
                                true
                        )
                );
            }
        }, 0L, 20L);




    }


    /**
     *
     * @param type
     * @param locations 3 Locations - (Ranking-1, Ranking-2, Ranking-3)
     */
    public void update(RankingType type, Location[] locations) {
        for (Entity entity : this.world.getEntities()) {
            if (entity.getType() != EntityType.ARMOR_STAND) continue;
            if (entity.getLocation().distance(locations[0]) < 10) {
                entity.remove();
            }
        }

        ArmorStand ranking1 = this.world.spawn(locations[0].clone().subtract(0, 0, 0.5), ArmorStand.class);
        ArmorStand ranking2 = this.world.spawn(locations[1].clone().subtract(0, 0, 0.5), ArmorStand.class);
        ArmorStand ranking3 = this.world.spawn(locations[2].clone().subtract(0, 0, 0.5), ArmorStand.class);

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

        Map<UUID, Long> statsMap = new HashMap<>();

        switch(type) {

            case KILLS -> {
                for (User user : plugin.getUserManager().getRepository().findAll()) {
                    if (user.getKills() < 1) continue;
                    statsMap.put(user.getUniqueId(), user.getKills());
                }
                break;
            }
            case TOKENS -> {
                for (User user : plugin.getUserManager().getRepository().findAll()) {
                    if (user.getBalance() < 1) continue;
                    statsMap.put(user.getUniqueId(), user.getBalance());
                }
                break;
            }
            case DEATHS -> {
                for (User user : plugin.getUserManager().getRepository().findAll()) {
                    if (user.getDeaths() < 1) continue;
                    statsMap.put(user.getUniqueId(), user.getDeaths());
                }
                break;
            }
            case TROPHIES -> {
                for (User user : plugin.getUserManager().getRepository().findAll()) {
                    if (user.getTrophies() < 1) continue;
                    statsMap.put(user.getUniqueId(), user.getTrophies());
                }
                break;
            }
        }


        AtomicInteger integer = new AtomicInteger(1);
        for (Map.Entry<UUID, Long> entry : Util.sortMapByValue(statsMap).entrySet()) {
            UUID uuid = entry.getKey();
            long value = entry.getValue();

            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            String playerName = UUIDFetcher.getName(uuid);

            if (value < 1) continue;
            if (player == null) continue;

            String killsText = "Not set";

            switch(type) {

                case TOKENS -> {
                    killsText = (value > 1 ? "§8( §a%s Tokens §8)" : "§8( §a %s Token §8)");
                }
                case KILLS -> {
                    killsText = (value > 1 ? "§8( §a%s Kills §8)" : "§8( §a %s Kill §8)");
                }
                case DEATHS -> {
                    killsText = (value > 1 ? "§8( §a%s Tode §8)" : "§8( §a %s Tod §8)");
                }
                case TROPHIES -> {
                    killsText = (value > 1 ? "§8( §a%s Trophäen §8)" : "§8( §a %s Trophäe §8)");
                }
            }

            if (integer.get() == 1) {
                ranking1.setCustomName("§8#§51 §7" + playerName + String.format(killsText, NumberFormat.getInstance(Locale.GERMAN).format(value)));
                ranking1.setHelmet(plugin.getSkullLoader().getSkull(uuid));
                ranking1.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                ranking1.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                ranking1.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
                ranking1.setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
            }
            if (integer.get() == 2) {
                ranking2.setCustomName("§8#§52 §7" + playerName + String.format(killsText, NumberFormat.getInstance(Locale.GERMAN).format(value)));
                ranking2.setHelmet(plugin.getSkullLoader().getSkull(uuid));
                ranking2.setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
                ranking2.setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
                ranking2.setBoots(new ItemStack(Material.GOLD_BOOTS));
                ranking2.setItemInHand(new ItemStack(Material.BOW));
            }
            if (integer.get() == 3) {
                ranking3.setCustomName("§8#§53 §7" + playerName + String.format(killsText, NumberFormat.getInstance(Locale.GERMAN).format(value)));
                ranking3.setHelmet(plugin.getSkullLoader().getSkull(uuid));
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
