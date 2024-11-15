package de.skymyth.rewards;

import de.skymyth.SkyMythPlugin;
import de.skymyth.location.model.Position;
import de.skymyth.rewards.model.Reward;
import de.skymyth.rewards.repository.RewardRepository;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.item.ItemBuilder;
import de.skymyth.utility.item.SkullCreator;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.DisableCause;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class RewardsManager {

    SkyMythPlugin plugin;
    Position hologramPosition;
    Hologram hologram;
    BukkitTask hologramTask;

    RewardRepository rewardRepository;
    List<Reward> rewards;

    boolean isInUse;
    long lastOpened;
    UUID lastOpenedBy;

    public RewardsManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.hologramPosition = plugin.getLocationManager().getPosition("rewards-hologram");
        this.rewardRepository = plugin.getMongoManager().create(RewardRepository.class);
        this.rewards = new ArrayList<>(rewardRepository.findManyBySeason("Winter"));

        this.hologram = DHAPI.createHologram("rewards-hologram", this.hologramPosition.toBukkitLocation().clone().add(0, 1, 0), false, List.of(
                "§f§lW§c§li§f§ln§c§lt§f§le§c§lr §f§lR§c§le§f§lw§c§la§f§lr§c§ld§f§ls",
                "§r",
                "§7Aktuelle Saison: §e" + "Winter",
                "§7Letzte Öffnung: §cUnbekannt",
                "§r",
                "§7§oKlicke auf den Block unten,",
                "§7§o um deine Belohnung abzuholen."
        ));

        this.hologramTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (isInUse) return;
            if (lastOpened == 0 || lastOpenedBy == null) {
                DHAPI.setHologramLine(this.hologram, 3, "§7Letzte Öffnung: §cUnbekannt");
                return;
            }
            DHAPI.setHologramLine(this.hologram, 3, "§7Letzte Öffnung: §evor " + TimeUtil.beautifyTime(System.currentTimeMillis() - lastOpened, TimeUnit.MILLISECONDS, true, false));
        }, 0L, 20*5L);

    }

    public void openFor(Player player) {
        if (isInUse) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cEs werden bereits Belohnungen von jemandem abgeholt.");
            return;
        }
        isInUse = true;

        this.hologram.disable(DisableCause.API);

        ArmorStand armorStand = player.getWorld().spawn(plugin.getLocationManager().getPosition("rewards-armorstand").toBukkitLocation().clone().add(0, -0.5, 0), ArmorStand.class);
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName("§7Belohnung wird geladen...");

        ItemBuilder helmet = new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjFiYzlkNDJiMDA0MWU4Zjk1Y2I5YjI2NjI4ZmRhZjUwY2QwZTM2ZjdiYjlkNmIzYTRkMmFmMzk0OWRhOTdkNiJ9fX0="));
        armorStand.setHelmet(helmet.clone());

        AtomicLong ticksElapsed = new AtomicLong();

        BukkitTask armorStandRotationTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            armorStand.setHeadPose(armorStand.getHeadPose().add(0, 0.1, 0));
            armorStand.teleport(armorStand.getLocation().clone().add(0, 0.05, 0));
            ticksElapsed.getAndIncrement();
        }, 0L, 1L);

        BukkitTask armorStandRotationTaskTwo = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            armorStandRotationTask.cancel();
            armorStand.setHeadPose(armorStand.getHeadPose().add(0, 0.3, 0));
        }, 20*3L, 1L);

        BukkitTask armorStandExplosionTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Location armorStandHeadLocation = armorStand.getEyeLocation();
            armorStandRotationTaskTwo.cancel();
            player.playEffect(armorStand.getLocation().clone(), Effect.EXPLOSION_LARGE, 0);
            armorStand.remove();
            for (int i = 0; i < 3; i++) {
                Reward reward = getRandomRewardWithChance();
                if (reward == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cEs gab ein Problem beim Laden deiner Belohnung §8#§c" + i + ".");
                    continue;
                }
                armorStandHeadLocation.getWorld().dropItem(armorStandHeadLocation, reward.getItem());
            }
            lastOpened = System.currentTimeMillis();
            isInUse = false;
            hologram.enable();
        }, 20*5L);

    }

    private Reward getRandomRewardWithChance() {
        List<Reward> rewards = new ArrayList<>();
        for (Reward reward : this.rewards) {
            if (reward.getChance() > Math.random()) {
                rewards.add(reward);
            }
        }
        if (rewards.isEmpty()) {
            return null;
        }
        return rewards.get((int) (Math.random() * rewards.size()));
    }

    private String getCurrentSeason() {
        Date date = new Date();
        int month = date.getMonth();
        if (month == 11 || month == 0 || month == 1) {
            return "Winter";
        } else if (month == 2 || month == 3 || month == 4) {
            return "Frühling";
        } else if (month == 5 || month == 6 || month == 7) {
            return "Sommer";
        } else {
            return "Herbst";
        }
    }

}
