package de.skymyth.giveaway.title;

import de.skymyth.SkyMythPlugin;
import de.skymyth.giveaway.title.part.TitlePart;
import de.skymyth.utility.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RandomPlayerScrambleTitle {

    BukkitTask titleTask;
    SkyMythPlugin plugin;

    public RandomPlayerScrambleTitle(SkyMythPlugin plugin) {
        this.plugin = plugin;
    }

    public void showScrambleTitle(String playerNameToScramble, Consumer<Player> winnerConsumer) {
        String[] nameParts = playerNameToScramble.split("");
        List<TitlePart> titleParts = TitlePart.createTitleParts(nameParts);

        titleTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            TitlePart randomPart = getRandomTitlePart(titleParts);
            if (randomPart != null) {
                randomPart.setRevealed(true);
            } else {
                startWinnerTask(playerNameToScramble, winnerConsumer);
                titleTask.cancel();
                return;
            }

            String scrambledName = titleParts.stream().map(part -> part.isRevealed() ? "§6" + part.getCharacter() + "§r" : "§6§k" + part.getCharacter() + "§r").collect(Collectors.joining());

            Bukkit.getOnlinePlayers().forEach(player -> {
                TitleUtil.sendTitle(player, 0, 25, 20, scrambledName, "§8× §7Gewinner §8×");
                player.playSound(player.getLocation(), Sound.STEP_SNOW, 1, 1);
            });
        }, 0, 20);
    }

    private void startWinnerTask(String playerNameToScramble, Consumer<Player> winnerConsumer) {
        AtomicInteger counter = new AtomicInteger(0);
        BukkitTask winnerTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            counter.getAndIncrement();
            String colorCode = (counter.get() % 2 == 0) ? "§6§n" : "§e§n";
            Bukkit.getOnlinePlayers().forEach(player -> {
                TitleUtil.sendTitle(player, 0, 25, 20, colorCode + playerNameToScramble + "§r", "§8× §7Gewinner §8×");
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 2, 2);
            });
        }, 0, 5);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            winnerTask.cancel();
            winnerConsumer.accept(Bukkit.getPlayer(playerNameToScramble));
        }, 75);
    }

    private TitlePart getRandomTitlePart(List<TitlePart> titleParts) {
        return titleParts.stream().sorted((a, b) -> Double.compare(Math.random(), 0.5)).filter(titlePart -> !titlePart.isRevealed()).findAny().orElse(null);
    }

}
