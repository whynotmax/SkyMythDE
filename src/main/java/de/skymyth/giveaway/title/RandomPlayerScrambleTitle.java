package de.skymyth.giveaway.title;

import de.skymyth.SkyMythPlugin;
import de.skymyth.giveaway.title.part.TitlePart;
import de.skymyth.utility.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

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
                winnerConsumer.accept(Bukkit.getPlayer(playerNameToScramble));
                AtomicInteger counter = new AtomicInteger(0);
                BukkitTask winnerTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                    counter.getAndIncrement();
                    if (counter.get() % 2 == 0) {
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            TitleUtil.sendTitle(player, 0, 25, 20, "§6§n" + playerNameToScramble + "§r", "§7Gewinner");
                        });
                        return;
                    }
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        TitleUtil.sendTitle(player, 0, 25, 20, "§e§n" + playerNameToScramble + "§r", "§7Gewinner");
                    });
                }, 0, 5);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    winnerTask.cancel();
                    winnerConsumer.accept(Bukkit.getPlayer(playerNameToScramble));
                }, 75);
                titleTask.cancel();
                return;
            }

            StringBuilder scrambledName = new StringBuilder();
            for (TitlePart part : titleParts) {
                if (part.isRevealed()) {
                    scrambledName.append("§6").append(part.getCharacter()).append("§r");
                } else {
                    scrambledName.append("§6§k").append(part.getCharacter()).append("§r");
                }
            }

            Bukkit.getOnlinePlayers().forEach(player -> {
                TitleUtil.sendTitle(player, 0, 25, 20, scrambledName.toString(), "§7Gewinner");
            });
        }, 0, 20);

    }

    private TitlePart getRandomTitlePart(List<TitlePart> titleParts) {
        return titleParts.stream()
                .sorted((a, b) -> Double.compare(Math.random(), 0.5))
                .filter(titlePart -> !titlePart.isRevealed())
                .findAny()
                .orElse(null);
    }

}
