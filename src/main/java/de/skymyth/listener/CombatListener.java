package de.skymyth.listener;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.TitleUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
@SuppressWarnings("all")
public class CombatListener implements Listener {

    SkyMythPlugin plugin;
    Cache<Player, Player> combat = CacheBuilder.newBuilder().expireAfterWrite(20, TimeUnit.SECONDS).build();
    Cache<Player, Long> combatTicker = CacheBuilder.newBuilder().expireAfterWrite(20, TimeUnit.SECONDS).build();
    List<String> blockedCommands = new ArrayList<>();


    public CombatListener(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.blockedCommands.add("/warp");
        this.blockedCommands.add("/spawn");
        this.blockedCommands.add("/home");
        this.blockedCommands.add("/tpa");
        this.blockedCommands.add("/tpahere");
        this.blockedCommands.add("/tpaccept");
        this.blockedCommands.add("/ec");
        this.blockedCommands.add("/enderchest");
        this.blockedCommands.add("/lager");

        Bukkit.getPluginManager().registerEvents(this, plugin);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (combat.getIfPresent(onlinePlayer) != null && combatTicker.getIfPresent(onlinePlayer) != null) {

                    Player target = combat.getIfPresent(onlinePlayer);
                    long inCombat = (combatTicker.getIfPresent(onlinePlayer) - System.currentTimeMillis());

                    if (inCombat < 1) {
                        onlinePlayer.sendMessage(SkyMythPlugin.PREFIX + "§cDu bist nun nicht mehr im Kampf.");
                        combat.invalidate(onlinePlayer);
                        combatTicker.invalidate(onlinePlayer);
                    }

                    if (inCombat > 1) {
                        TitleUtil.sendActionBar(onlinePlayer, "§cDu bist noch " + (TimeUtil
                                .beautifyTime(inCombat, TimeUnit.MILLISECONDS, true, true)) + " §cim Kampf.");
                        TitleUtil.sendActionBar(target, "§cDu bist noch " + (TimeUtil
                                .beautifyTime(inCombat, TimeUnit.MILLISECONDS, true, true)) + " §cim Kampf.");
                    }
                }
            }
        }, 0L, 20L);
    }

    public boolean isInCombat(Player player) {
        if (combatTicker.getIfPresent(player) == null) return false;
        long inCombat = (combatTicker.getIfPresent(player) - System.currentTimeMillis());
        return inCombat > 1;
    }

    public String getRemaining(Player player) {
        return TimeUtil.beautifyTime(combatTicker.getIfPresent(player) - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS, true, true);
    }

    public Player getEnemy(Player player) {
        if (combat.getIfPresent(player) != null) {
            return combat.getIfPresent(player);
        }
        return null;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        if (event.getEntity() instanceof Player player) {

            User playerUser = plugin.getUserManager().getUser(player.getUniqueId());
            playerUser.addDeath();


            if (combat.getIfPresent(player) != null) {
                long inCombat = (combatTicker.getIfPresent(player) - System.currentTimeMillis());
                Player attacker = (combat.getIfPresent(player));

                User attackerUser = plugin.getUserManager().getUser(attacker.getUniqueId());

                if (inCombat > 1) {
                    combat.invalidate(player);
                    combatTicker.invalidate(player);

                    combat.invalidate(attacker);
                    combatTicker.invalidate(attacker);

                    attackerUser.addKill();
                    attackerUser.addPvPShards(5);
                    attackerUser.addTrophies(10);

                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu wurdest von " + attacker.getName() + " getötet §8(§c" + Math.round(attacker.getHealth() / 2) + "❤§8)");


                    attacker.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast " + player.getName() + " getötet.");
                    attacker.sendMessage(SkyMythPlugin.PREFIX + "§eTrophäen: " + attackerUser.getTrophies() + " §a(+10)");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();


        if (combat.getIfPresent(player) != null) {
            long inCombat = (combatTicker.getIfPresent(player) - System.currentTimeMillis());

            if (inCombat > 0) {
                player.setHealth(0);

                combat.invalidate(player);
                combatTicker.invalidate(player);

                for (Player onlinePlayers : player.getWorld().getPlayers()) {
                    onlinePlayers.sendMessage(SkyMythPlugin.PREFIX + "§c" + player.getName() + " hat sich im Kampf ausgeloggt.");
                }
            }

        }
    }

    public void startCombat(Player player, Player target) {

        if (player.getGameMode() == GameMode.CREATIVE || target.getGameMode() == GameMode.CREATIVE) return;

        if (combat.getIfPresent(player) == null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§aDu bist jetzt im Kampf.");
        }
        if (combat.getIfPresent(target) == null) {
            target.sendMessage(SkyMythPlugin.PREFIX + "§aDu bist jetzt im Kampf.");
        }
        combat.put(player, target);
        combat.put(target, player);
        combatTicker.put(player, (System.currentTimeMillis() + 15000));
        combatTicker.put(target, (System.currentTimeMillis() + 15000));

    }


}
