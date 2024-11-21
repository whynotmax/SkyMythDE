package de.skymyth.scoreboard;

import de.skymyth.SkyMythPlugin;
import de.skymyth.baseprotector.model.BaseProtector;
import de.skymyth.user.model.User;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.TitleUtil;
import de.skymyth.utility.Util;
import fr.mrmicky.fastboard.FastBoard;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

        if(fastBoard == null) return;

        plugin.getTablistManager().setRank(player);
        user.updatePlayTime();

        String playerWorld = player.getWorld().getName();
        if (plugin.getCombatListener().isInCombat(player)) {
            fastBoard.updateLines(
                    "",
                    "§f⚔ §8┃ §7Rüstung",
                    "  §8× §f" + getDurability(player.getInventory().getHelmet()),
                    "  §8× §f" + getDurability(player.getInventory().getChestplate()),
                    "  §8× §f" + getDurability(player.getInventory().getLeggings()),
                    "  §8× §f" + getDurability(player.getInventory().getBoots()),
                    "",
                    "§fΩ §8┃ §7Gegner",
                    "  §8× §c" + plugin.getCombatListener().getEnemy(player).getName(),
                    "  §8× §c" + plugin.getCombatListener().getEnemy(player).getHealth() + "/" + plugin.getCombatListener().getEnemy(player).getMaxHealth(),
                    "",
                    "§f✄ §8┃ §7Verbleibend",
                    "  §8× §c" + TimeUtil.beautifyTime(plugin.getCombatListener().getRemainingTime(player), TimeUnit.SECONDS, true, true),
                    ""
            );
            return;
        }
        if (playerWorld.equals("PvP")) {
            fastBoard.updateLines(
                    "",
                    "§f⚔ §8┃ §7Statistiken",
                    "  §8× §a" + user.getKills() + " §8/ §c" + user.getDeaths(),
                    "  §8× §e" + user.getKillDeathRatio(),
                    "",
                    "§f⛃ §8┃ §7Tokens",
                    "  §8× §f" + NumberFormat.getInstance(Locale.GERMAN).format(user.getBalance()),
                    "",
                    "§f❤ §8┃ §7PvP-Shards",
                    "  §8× §b" + NumberFormat.getInstance(Locale.GERMAN).format(user.getPvpShards()),
                    "",
                    "§f♛ §8┃ §7Trophäen",
                    "  §8× §6" + user.getTrophies(),
                    ""
            );
            return;
        }
        if (playerWorld.equals("world")) {
            BaseProtector baseProtector = plugin.getBaseProtectorManager().getBaseProtection(player.getLocation().getBlock());

            if (baseProtector != null) {
                TitleUtil.sendActionBar(player, "§7Du befindest dich im Basisschutz von §e" +
                        Bukkit.getOfflinePlayer(baseProtector.getBaseOwner()).getName());
            }
        }
        //TODO: Mine scoreboard?
        fastBoard.updateLines(
                "",
                "§f⚔ §8┃ §7Statistiken",
                "  §8× §a" + user.getKills() + " §8/ §c" + user.getDeaths(),
                "  §8× §e" + user.getKillDeathRatio(),
                "",
                "§f⛃ §8┃ §7Tokens",
                "  §8× §f" + NumberFormat.getInstance(Locale.GERMAN).format(user.getBalance()),
                "",
                "§f❤ §8┃ §7Online",
                "  §8× §a" + (Bukkit.getOnlinePlayers().size() - Util.VANISH.size()) + " §8/ §c" + Bukkit.getMaxPlayers(),
                "",
                "§f♛ §8┃ §7Trophäen",
                "  §8× §6" + user.getTrophies(),
                ""
        );
    }

    private String getDurability(ItemStack itemStack) {
        String perfect = "§a";
        String good = "§2";
        String middle = "§e";
        String bad = "§c";
        String ugly = "§4";

        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return "§c✘";
        }

        double durability = (double) itemStack.getDurability() / itemStack.getType().getMaxDurability();

        if (durability >= 0.8) {
            return perfect + "✦ §8(§e" + String.format("%.2f", (durability * 100)) + "%§8)";
        } else if (durability >= 0.6) {
            return good + "✦ §8(§e" + String.format("%.2f", (durability * 100)) + "%§8)";
        } else if (durability >= 0.4) {
            return middle + "✦ §8(§e" + String.format("%.2f", (durability * 100)) + "%§8)";
        } else if (durability >= 0.2) {
            return bad + "✦ §8(§e" + String.format("%.2f", (durability * 100)) + "%§8)";
        } else {
            return ugly + "✦ §8(§e" + String.format("%.2f", (durability * 100)) + "%§8)";
        }

    }
}
