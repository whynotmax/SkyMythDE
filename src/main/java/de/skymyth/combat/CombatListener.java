package de.skymyth.combat;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;
import de.skymyth.utility.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class CombatListener implements Listener {

    SkyMythPlugin plugin;
    Map<Player, Player> combatMap;
    Map<Player, Long> lastHitMap;
    Map<Player, BukkitTask> combatTaskMap;

    public CombatListener(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.combatMap = new HashMap<>();
        this.combatTaskMap = new HashMap<>();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    public void hit(Player player) {
        this.lastHitMap.put(player, System.currentTimeMillis());
    }

    public long getRemainingTime(Player player) {
        return 15000 - (System.currentTimeMillis() - this.lastHitMap.get(player));
    }

    public void startCombat(Player player1, Player player2) {
        this.combatMap.put(player1, player2);
        this.combatMap.put(player2, player1);

        BukkitTask task = this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> {

            if (this.combatMap.get(player1) == null || this.combatMap.get(player2) == null) {
                this.combatTaskMap.get(player1).cancel();
                this.combatTaskMap.get(player2).cancel();
                this.combatTaskMap.remove(player1);
                this.combatTaskMap.remove(player2);
                return;
            }
            if (System.currentTimeMillis() - this.lastHitMap.get(player1) > 15000) {
                this.combatTaskMap.get(player1).cancel();
                this.combatTaskMap.get(player2).cancel();
                this.combatTaskMap.remove(player1);
                this.combatTaskMap.remove(player2);
                this.combatMap.remove(player1);
                this.combatMap.remove(player2);
            }

            Player target1 = this.combatMap.get(player1);
            Player target2 = this.combatMap.get(player2);

            if (target2 == null || target1 == null) {
                this.combatTaskMap.get(player1).cancel();
                this.combatTaskMap.get(player2).cancel();
                this.combatTaskMap.remove(player1);
                this.combatTaskMap.remove(player2);
                this.combatMap.remove(player1);
                this.combatMap.remove(player2);
                return;
            }

            TitleUtil.sendActionBar(target1, "§cDu bist im Kampf mit " + target2.getName() + " §8(§e" + getRemainingTime(player1) + "§8)");
            TitleUtil.sendActionBar(target2, "§cDu bist im Kampf mit " + target1.getName() + " §8(§e" + getRemainingTime(player2) + "§8)");
        }, 0L, 20);

        this.combatTaskMap.put(player1, task);
        this.combatTaskMap.put(player2, task);
    }

    public boolean isInCombat(Player player) {
        return this.combatMap.containsKey(player);
    }

    @EventHandler
    public void onHit(final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;
        Player damager = (Player) event.getDamager();
        Player target = (Player) event.getEntity();
        User damagerUser = this.plugin.getUserManager().getUser(damager.getUniqueId());
        User targetUser = this.plugin.getUserManager().getUser(target.getUniqueId());
        if (isInCombat(damager) || isInCombat(target)) {
            event.setCancelled(true);
            return;
        }
        this.hit(damager);
        this.hit(target);
        this.startCombat(damager, target);
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (isInCombat(player)) {
            Player target = this.combatMap.get(player);
            this.combatTaskMap.get(player).cancel();
            this.combatTaskMap.get(target).cancel();
            this.combatTaskMap.remove(player);
            this.combatTaskMap.remove(target);
            this.combatMap.remove(player);
            this.combatMap.remove(target);

            TitleUtil.sendActionBar(target, "§cDu bist nicht mehr im Kampf.");
            TitleUtil.sendTitle(target, 0, 10, 20, "§a" + player.getName() + " §7ist gestorben.", "§8× +§e1 Kill §8- +§e100 Tokens §8×");

            User targetUser = this.plugin.getUserManager().getUser(target.getUniqueId());
            targetUser.addKill();
            targetUser.addBalance(100);
            targetUser.addTrophies(150);
            this.plugin.getUserManager().saveUser(targetUser);

            User playerUser = this.plugin.getUserManager().getUser(player.getUniqueId());
            playerUser.addDeath();
            if (playerUser.getBalance() >= 100) {
                playerUser.removeBalance(100);
            } else {
                playerUser.setBalance(0);
            }
            if (playerUser.getTrophies() >= 150) {
                playerUser.removeTrophies(150);
            } else {
                playerUser.setTrophies(0);
            }
            this.plugin.getUserManager().saveUser(playerUser);

            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du wurdest von §e" + target.getName() + " §7getötet.");
            player.sendMessage(SkyMythPlugin.PREFIX + "§8-§c1 Kill §8-§c100 Tokens §8-§c150 Trophäen");

            target.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + player.getName() + " §7getötet.");
            target.sendMessage(SkyMythPlugin.PREFIX + "§8+§a1 Kill §8+§a100 Tokens §8+§a150 Trophäen");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLogout(final PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (isInCombat(player)) {
            Player target = this.combatMap.get(player);
            this.combatTaskMap.get(player).cancel();
            this.combatTaskMap.get(target).cancel();
            this.combatTaskMap.remove(player);
            this.combatTaskMap.remove(target);
            this.combatMap.remove(player);
            this.combatMap.remove(target);

            TitleUtil.sendActionBar(target, "§cDu bist nicht mehr im Kampf.");
            TitleUtil.sendTitle(target, 0, 10, 20, "§a" + player.getName() + " §7ist gestorben.", "§8× +§e1 Kill §8- +§e100 Tokens §8×");

            User targetUser = this.plugin.getUserManager().getUser(target.getUniqueId());
            targetUser.addKill();
            targetUser.addBalance(100);
            targetUser.addTrophies(150);
            this.plugin.getUserManager().saveUser(targetUser);

            User playerUser = this.plugin.getUserManager().getUser(player.getUniqueId());
            playerUser.addDeath();
            if (playerUser.getBalance() >= 100) {
                playerUser.removeBalance(100);
            } else {
                playerUser.setBalance(0);
            }
            if (playerUser.getTrophies() >= 150) {
                playerUser.removeTrophies(150);
            } else {
                playerUser.setTrophies(0);
            }
            this.plugin.getUserManager().saveUser(playerUser);

            target.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + player.getName() + " §7getötet.");
            target.sendMessage(SkyMythPlugin.PREFIX + "§8+§a1 Kill §8+§a100 Tokens §8+§a150 Trophäen");

            Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§e" + player.getName() + " §7hat sich im Kampf ausgeloggt.");
        }
    }

    private String getHearts(Player player) {
        double health = player.getHealth();
        int hearts = (int) Math.ceil(health / 2);
        StringBuilder builder = new StringBuilder();
        builder.append("❤".repeat(Math.max(0, hearts)));
        String healthString = String.format("%.1f", health);
        return "§c" + builder.toString() + "§8 (§e" + healthString + "§8)";
    }

}
