package de.skymyth.listener;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public class CombatListener implements Listener {

    SkyMythPlugin plugin;
    Cache<Player, Player> combat = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.SECONDS).build();
    Cache<Player, Long> combatTicker = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.SECONDS).build();


    public CombatListener(SkyMythPlugin plugin) {
        this.plugin = plugin;

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if(combat.getIfPresent(onlinePlayer) != null && combatTicker.getIfPresent(onlinePlayer) != null) {

                    Player target = combat.getIfPresent(onlinePlayer);
                    long inCombat = (combatTicker.getIfPresent(onlinePlayer)-System.currentTimeMillis());

                    if(inCombat <= 0) {
                        combat.invalidate(onlinePlayer);
                        combatTicker.invalidate(onlinePlayer);
                        onlinePlayer.sendMessage(SkyMythPlugin.PREFIX + "§cDu bist nun nicht mehr im Kampf.");
                        continue;
                    }
                    TitleUtil.sendActionBar(onlinePlayer, "§cDu bist noch " + (TimeUtil
                            .beautifyTime(inCombat, TimeUnit.MILLISECONDS, true, true)) + " §cim Kampf.");
                    TitleUtil.sendActionBar(target, "§cDu bist noch " + (TimeUtil
                            .beautifyTime(inCombat, TimeUnit.MILLISECONDS, true, true)) + " §cim Kampf.");
                }
            }
        },0L,20L);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(combat.getIfPresent(player) != null) {
                long inCombat = (combatTicker.getIfPresent(player) - System.currentTimeMillis());
            }


        }
    }

    public void startCombat(Player player, Player target) {
        combat.put(player, target);
        combatTicker.put(player, (System.currentTimeMillis()+15000));

        if(combat.getIfPresent(player) == null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§aDu bist jetzt im Kampf.");
        }
        if(combat.getIfPresent(target) == null) {
            target.sendMessage(SkyMythPlugin.PREFIX + "§aDu bist jetzt im Kampf.");
        }

    }


}
