package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.Util;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public record PlayerJoinListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void on(PlayerSpawnLocationEvent event) {
        event.setSpawnLocation(plugin.getLocationManager().getPosition("spawn").getLocation());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.getPlayerCount().fastPut("skypvp", (Bukkit.getOnlinePlayers().size() - Util.VANISH.size()));


        plugin.getUserManager().loadUser(player.getUniqueId());
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        user.setWasOnlineToday(true);

        event.setJoinMessage(null);
        plugin.getScoreboardManager().createScoreboard(player);
        plugin.getTablistManager().setRank(player);

        for (int i = 0; i < 50; i++) {
            player.sendMessage("§" + String.valueOf(i).charAt(0));
        }


        player.sendMessage("§8§m----------------------------------------§r");
        player.sendMessage("§r ");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Willkommen auf §5§lSkyMyth§8.§5§lDE§7!");
        if ((user.getLastSeen() > (System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000))) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du warst offline für §c" + TimeUtil.beautifyTime(System.currentTimeMillis() - user.getLastSeen(), TimeUnit.MILLISECONDS, true, true) + "§7.");
        }
        player.sendMessage("§r ");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Spenden§8: §chttps://store.skymyth.de/");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Tutorial§8: /§ctutorial");
        player.sendMessage("§r ");
        player.sendMessage("§8§m----------------------------------------§r");

        user.setLastSeen(System.currentTimeMillis());
        plugin.getSkullLoader().addSkull(player.getUniqueId());
        player.setFoodLevel(20);

        if (player.isOp()) {
            Util.COMMANDWATCHER.add(player);
        }

        player.setGameMode((player.isOp() ? GameMode.CREATIVE : GameMode.SURVIVAL));
        player.playSound(player.getLocation().clone().add(0, 10, 0), Sound.ENDERDRAGON_HIT, 1, 1);


        if (!player.hasPlayedBefore()) {
            Bukkit.broadcastMessage("§r");
            Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Willkommen §e" + player.getName() + "§7 auf §5§lSkyMyth§8.§5§lDE§7!");
            Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§a§lNEU! §8× #§e" + NumberFormat.getInstance(Locale.GERMAN).format(Bukkit.getOfflinePlayers().length) + " Spieler §7registriert.");
            Bukkit.broadcastMessage("§r");
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer == player) continue;

                onlinePlayer.sendMessage(SkyMythPlugin.PREFIX + "§eAlle Spieler§7 haben §e200 Tokens §7erhalten.");
                User onlineUser = plugin.getUserManager().getUser(onlinePlayer.getUniqueId());
                onlineUser.addBalance(200);
                onlinePlayer.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e200 Tokens §7erhalten.");
            }
            plugin.getKitManager().getKitByName("Neuling").giveTo(user, plugin);
            player.getInventory().addItem(plugin.getBaseProtectorManager().getBaseProtectorItem());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate give PvP " + player.getName() + " 16");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate give Blocks " + player.getName() + " 16");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate give Tokens " + player.getName() + " 16");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate give Tools " + player.getName() + " 6");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate give AoN " + player.getName() + " 2");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate give Emoji " + player.getName() + " 2");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate give christmas24 " + player.getName() + " 3");
        }


        if (user.hasJoinMessage()) {
            Bukkit.broadcastMessage("§8[§a+§8] §7" + user.getJoinMessage().replace('&', '§').replace("§k", "&k"));
        }

        if (user.getTrophiesLostDueToInactivity() != 0) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Da du lange inaktiv warst, hast du");
            player.sendMessage(SkyMythPlugin.PREFIX + "§e" + user.getTrophiesLostDueToInactivity() + " Trophäen §7verloren.");
            player.sendMessage(SkyMythPlugin.PREFIX + "§6" + (user.getTrophies() + user.getTrophiesLostDueToInactivity()) + " §e(-" + user.getTrophiesLostDueToInactivity() + ")");
            user.setTrophiesLostDueToInactivity(0);
        }
    }
}
