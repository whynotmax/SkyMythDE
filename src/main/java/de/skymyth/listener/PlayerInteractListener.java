package de.skymyth.listener;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.skymyth.SkyMythPlugin;
import de.skymyth.badge.model.Badge;
import de.skymyth.baseprotector.model.BaseProtector;
import de.skymyth.baseprotector.ui.BaseMainInventory;
import de.skymyth.kit.model.Kit;
import de.skymyth.ui.EnchanterInventory;
import de.skymyth.user.model.User;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.TitleUtil;
import de.skymyth.utility.Util;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

public class PlayerInteractListener implements Listener {

    SkyMythPlugin plugin;
    Cache<Player, Arrow> sniperArrow;

    public PlayerInteractListener(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.sniperArrow = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.SECONDS).build();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = new ItemBuilder(player.getItemInHand());

        if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.getItemMeta().hasLore() &&
                itemStack.getItemMeta().getLore().size() >= 3 &&
                itemStack.getItemMeta().getLore().get(1).contains("gutschein")) {
            event.setCancelled(true);
            String gutschein = itemStack.getItemMeta().getLore().get(1).replace("§7Mit diesem ", "").replace("gutschein kannst du", "");
            switch (gutschein) {
                case "Token" -> {
                    long balance = Long.parseLong(itemStack.getItemMeta().getLore().get(2).replace("§7dir §e", "").replace(" §7Tokens", "").replace(".", ""));
                    User user = plugin.getUserManager().getUser(player.getUniqueId());
                    user.addBalance(balance);
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + balance + " §7Tokens erhalten.");
                    Util.removeItem(player, itemStack);
                    break;
                }
                case "Kit" -> {
                    String kitName = itemStack.getItemMeta().getLore().get(2)
                            .replace("§7dir das Kit §8'§e", "")
                            .replace("§8' §7einmal einlösen.", "");
                    Kit kit = plugin.getKitManager().getKitByName(kitName);
                    if (kit == null) {
                        player.sendMessage(SkyMythPlugin.PREFIX + "§cDieses Kit existiert nicht.");
                        return;
                    }
                    kit.giveToAsVoucher(plugin.getUserManager().getUser(player.getUniqueId()), false);
                    Util.removeItem(player, itemStack);
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast das Kit §e" + kitName + " §7erhalten.");
                    break;
                }
                case "Badge" -> {
                    String badgeName = itemStack.getItemMeta().getLore().get(2).replace("§7dir das Badge §8'§e", "").replace("§8' §7freischalten.", "");
                    Badge badge = plugin.getBadgeManager().getBadgeByCharacter(badgeName);
                    if (badge == null) {
                        player.sendMessage(SkyMythPlugin.PREFIX + "§cDieses Badge existiert nicht.");
                        return;
                    }
                    if (badge.getOwners().contains(player.getUniqueId())) {
                        player.sendMessage(SkyMythPlugin.PREFIX + "§cDu besitzt dieses Badge bereits.");
                        return;
                    }
                    badge.getOwners().add(player.getUniqueId());
                    plugin.getBadgeManager().saveBadge(badge);
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast das Badge §e" + badgeName + " §7erhalten.");
                    Util.removeItem(player, itemStack);
                    break;
                }
            }
            return;
        }

        if (player.getWorld().getName().equals("FpsArena")) {
            if (player.getItemInHand() != null) {
                if (player.getItemInHand().getType() == Material.ENDER_PEARL &&
                        !player.isOp()) {
                    event.setUseItemInHand(Event.Result.DENY);
                    event.setCancelled(true);
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Dieses Item ist hier nicht erlaubt.");
                }
            }
        }

        if (player.getWorld().getName().equals("Spawn")) {
            if (player.getItemInHand() != null) {
                if (player.getItemInHand().getType() == Material.BOW
                        || player.getItemInHand().getType() == Material.ENDER_PEARL
                        || player.getItemInHand().getType() == Material.POTION
                        && !player.isOp()) {
                    event.setUseItemInHand(Event.Result.DENY);
                    event.setCancelled(true);
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Dieses Item ist hier nicht erlaubt.");
                }
            }

            Block block = event.getClickedBlock();
            if (block == null) return;

            if (block.getType() == Material.ENCHANTMENT_TABLE) {
                event.setCancelled(true);
                plugin.getInventoryManager().openInventory(player, new EnchanterInventory(plugin));
                return;
            }

            if (block.getType() != Material.ENDER_PORTAL_FRAME) return;

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                User user = plugin.getUserManager().getUser(player.getUniqueId());
                if (user.isOnCooldown("dailyReward")) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst noch " + TimeUtil.beautifyTime(user.getCooldown("dailyReward").getRemainingTime(), TimeUnit.MILLISECONDS, true, true) + " warten.");
                    return;
                }

                plugin.getRewardsManager().openFor(player);
            }
        }

        if (player.getWorld().getName().equals("world")) {

            Block block = event.getClickedBlock();

            if (block != null && event.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.ENDER_PORTAL_FRAME) {
                BaseProtector baseProtector = plugin.getBaseProtectorManager().getBaseProtection(block);

                if (baseProtector != null) {
                    if (baseProtector.getBaseOwner().equals(player.getUniqueId())) {
                        plugin.getInventoryManager().openInventory(player, new BaseMainInventory(player, plugin));
                    }
                }
                return;
            }

            if (block != null && plugin.getBaseProtectorManager().isBlockProtected(block)) {
                BaseProtector baseProtector = plugin.getBaseProtectorManager().getBaseProtection(block);

                if (player.isOp()) {
                    return;
                }

                if (!baseProtector.getTrustedPlayers().contains(player.getUniqueId()) && !baseProtector.getBaseOwner().equals(player.getUniqueId())) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDie Base von " + Bukkit.getOfflinePlayer(baseProtector.getBaseOwner()).getName() + " §cist geschützt.");
                    event.setCancelled(true);
                    return;
                }
                return;
            }
        }

        if (player.getItemInHand() != null && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§b§lSniper")) {
            event.setCancelled(true);

            if (this.sniperArrow.getIfPresent(player) != null) {
                TitleUtil.sendActionBar(player, "§cBitte warte noch einen kurzen Moment!");
                return;
            }
            Arrow arrow = player.launchProjectile(Arrow.class);
            arrow.setKnockbackStrength(2);
            player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability() + 1));
            this.sniperArrow.put(player, arrow);
        }
    }

}
