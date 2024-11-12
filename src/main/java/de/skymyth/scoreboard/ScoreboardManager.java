package de.skymyth.scoreboard;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;
import fr.mrmicky.fastboard.FastBoard;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreboardManager {

    SkyMythPlugin plugin;
    Map<UUID, FastBoard> fastBoardMap;

    public ScoreboardManager(SkyMythPlugin plugin) {
        this.fastBoardMap = new HashMap<>();
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskTimer(plugin, () -> Bukkit.getOnlinePlayers().forEach(this::updateScoreboard), 0L, 20L);
    }

    public void createScoreboard(Player player) {
        FastBoard fastBoard = new FastBoard(player);
        fastBoard.updateTitle("§5§lSkyMyth");
        this.fastBoardMap.put(player.getUniqueId(), fastBoard);
    }

    public void destroyScoreboard(Player player) {
        this.fastBoardMap.get(player.getUniqueId()).delete();
    }

    public void updateScoreboard(Player player) {
        FastBoard fastBoard = this.fastBoardMap.get(player.getUniqueId());
        User user = plugin.getUserManager().getUser(player.getUniqueId());

        fastBoard.updateLines(
                "",
                "§f⚔ §8┃ §7Statistiken",
                "  §8× §a" + user.getKills() + " §8/ §c" + user.getDeaths(),
                "",
                "§f$ §8┃ §7Tokens",
                "  §8× §f" + NumberFormat.getInstance(Locale.GERMAN).format(user.getBalance()),
                "",
                "§f❤ §8┃ §7Online",
                "  §8× §a" + Bukkit.getOnlinePlayers().size() + " §8/ §c" + Bukkit.getMaxPlayers(),
                "",
                "§f♛ §8┃ §7Trophäen",
                "  §8× §6" + user.getTrophies()
        );
    }
}
