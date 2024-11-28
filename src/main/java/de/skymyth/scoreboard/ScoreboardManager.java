package de.skymyth.scoreboard;

import de.skymyth.SkyMythPlugin;
import de.skymyth.perks.model.Perks;
import de.skymyth.pvp.model.PvPRank;
import de.skymyth.user.model.User;
import de.skymyth.utility.Util;
import fr.mrmicky.fastboard.FastBoard;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

        if (fastBoard == null || fastBoard.isDeleted()) return;

        plugin.getTablistManager().setRank(player);
        user.updatePlayTime();

        //Perk shit
        if (user.hasPerk(Perks.STRENTH)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 21, 1, false, false));
        }
        if (user.hasPerk(Perks.SPEED)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 21, 1, false, false));
        }
        if (user.hasPerk(Perks.JUMP_BOOST)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 21, 1, false, false));
        }
        if (user.hasPerk(Perks.INVISIBILITY)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 21, 1, false, false));
            //TODO: vielleicht nicht gerade nice auf der pvp map
        }

        String playerWorld = player.getWorld().getName();

        if (plugin.getCombatListener().isInCombat(player)) {
            fastBoard.updateLines(
                    "",
                    "§f⚔ §8┃ §7Statistiken",
                    "  §8× §a" + user.getKills() + " §8/ §c" + user.getDeaths(),
                    "  §8× §e" + user.getKillDeathRatio(),
                    "",
                    "§fΩ §8┃ §7Gegner",
                    "  §8× §c" + plugin.getCombatListener().getEnemy(player).getName(),
                    "  §8× §c" + Math.round(plugin.getCombatListener().getEnemy(player).getHealth() / 2) + " ❤",
                    "",
                    "§f✄ §8┃ §7Verbleibend",
                    "  §8× §c" + plugin.getCombatListener().getRemaining(player),
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

                    "  §8× §6" + user.getTrophies() + " §8• §f" + PvPRank.getRank(user.getTrophies()).getDisplayName(),
                    ""
            );
            return;
        }
        //FEAT: Mine scoreboard?
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
                "  §8× §6" + user.getTrophies() + " §8• §f" + PvPRank.getRank(user.getTrophies()).getDisplayName(),
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
            return perfect + "✦ §8(§e" + String.format("%.0f", (durability * 100)) + "%§8)";
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
