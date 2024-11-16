package de.skymyth.casino.dailypot;

import de.skymyth.SkyMythPlugin;
import de.skymyth.casino.dailypot.model.DailyPot;
import de.skymyth.casino.dailypot.repository.DailyPotRepository;
import de.skymyth.user.model.User;
import de.skymyth.utility.TimeUtil;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Getter
public class DailyPotManager {

    SkyMythPlugin plugin;

    DailyPot dailyPot;
    DailyPotRepository dailyPotRepository;

    Hologram hologram;
    BukkitTask hologramUpdateTask;

    BukkitTask potDrawTask;

    public DailyPotManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.dailyPotRepository = plugin.getMongoManager().create(DailyPotRepository.class);
        if (!dailyPotRepository.findAll().isEmpty()) this.dailyPot = dailyPotRepository.findAll().getFirst();
        if (dailyPot == null) {
            dailyPot = new DailyPot();
            dailyPot.setId("dailyPot");
            dailyPot.setPot(0);
            dailyPot.setLastPot(System.currentTimeMillis());
            dailyPot.setLastWinner(null);
            dailyPot.setLastWinnerPot(0);
            dailyPot.setParticipants(List.of());
            dailyPotRepository.save(dailyPot);
        }
        this.hologram = DHAPI.createHologram("dailypot-holo", plugin.getLocationManager().getPosition("dailypot-holo").toBukkitLocation(), List.of(
                "§6§lDailyPot §8× §7Informationen",
                "§r",
                "§7Letzter Gewinner: §cUnbekannt",
                "§7Gewinnchance: §cUnbekannt",
                "§7Gewinn: §cUnbekannt",
                "§7Nächste Ziehung in: §cUnbekannt",
                "§r",
                "§7Der DailyPot ist ein tägliches",
                "§7Event, bei dem du die Chance",
                "§7hast, den gesamten Pot zu gewinnen."
        ));

        this.hologramUpdateTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            DHAPI.setHologramLine(hologram, 2, "§7Letzter Gewinner: §e" + (dailyPot.getLastWinner() == null ? "§cUnbekannt" : Bukkit.getOfflinePlayer(dailyPot.getLastWinner()).getName()));
            DHAPI.setHologramLine(hologram, 3, "§7Gewinnchance: §e" + dailyPot.calculateWinChance() + "%");
            DHAPI.setHologramLine(hologram, 4, "§7Gewinn: §e" + NumberFormat.getInstance(Locale.GERMAN).format(dailyPot.getPot()).replace(",", ".") + "$");
            DHAPI.setHologramLine(hologram, 5, "§7Nächste Ziehung in: §e" + TimeUtil.beautifyTime((dailyPot.getLastPot() + 86400000 - System.currentTimeMillis()) / 1000, TimeUnit.SECONDS, true, true) + " Sekunden");
        }, 0L, 20*5L);

        this.potDrawTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (System.currentTimeMillis() - dailyPot.getLastPot() >= 86400000) {
                drawDailyPot();
            }
        }, 0L, 20*30L);

    }

    public void joinDailyPot(Player player) {
        if (dailyPot.getParticipants().contains(player.getUniqueId())) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu nimmst bereits am DailyPot teil.");
            return;
        }
        dailyPot.getParticipants().add(player.getUniqueId());
        long oldPot = dailyPot.getPot();
        long newPot = oldPot + 100;
        dailyPot.setPot(newPot);
        dailyPotRepository.save(dailyPot);
        player.sendMessage(SkyMythPlugin.PREFIX + "§aDu nimmst nun am DailyPot teil.");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Deine Gewinnchance beträgt §e" + dailyPot.calculateWinChance() + "%§7.");
    }

    public void drawDailyPot() {
        if (dailyPot.getParticipants().isEmpty()) {
            Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§cEs gibt keine Teilnehmer für den DailyPot.");
            return;
        }
        int winnerIndex = new Random().nextInt(dailyPot.getParticipants().size());
        dailyPot.setLastWinner(dailyPot.getParticipants().get(winnerIndex));
        User winner = plugin.getUserManager().getUser(dailyPot.getLastWinner());
        winner.addBalance(Long.parseLong(String.valueOf(dailyPot.getPot())));
        plugin.getUserManager().saveUser(winner);
        dailyPot.setLastWinnerPot(dailyPot.getPot());
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§aDer Gewinner des DailyPots wurde gezogen.");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Der Gewinner ist §e" + Bukkit.getOfflinePlayer(dailyPot.getLastWinner()).getName() + "§7.");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Der Gewinner hat §e" + NumberFormat.getInstance(Locale.GERMAN).format(dailyPot.getPot()).replace(",", ".") + "$ §7gewonnen.");
        dailyPot.setPot(0);
        dailyPot.setLastPot(System.currentTimeMillis());
        dailyPot.getParticipants().clear();
        dailyPotRepository.save(dailyPot);
    }

}
