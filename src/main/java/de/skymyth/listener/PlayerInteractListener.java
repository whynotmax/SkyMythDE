package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.badge.model.Badge;
import de.skymyth.kit.model.Kit;
import de.skymyth.protector.ProtectionManager;
import de.skymyth.protector.model.Protector;
import de.skymyth.user.model.User;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public record PlayerInteractListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemBuilder itemStack = new ItemBuilder(player.getItemInHand());
        if (player.getItemInHand() != null) {
            User user = plugin.getUserManager().getUser(player.getUniqueId());
            if (itemStack.getType() == Material.DOUBLE_PLANT && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().startsWith("§7Gutschein§8: §e")) {
                String[] split = itemStack.getItemMeta().getDisplayName().split("§e");
                if (split.length == 2) {
                    long amount = Long.parseLong(split[1].replace(" Coins", "").replace(".", ""));
                    amount *= itemStack.getAmount();
                    player.getInventory().remove(itemStack);
                    user.addBalance(amount);
                    plugin.getUserManager().saveUser(user);
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + NumberFormat.getInstance(Locale.GERMAN).format(amount).replace(",", ".") + " Coins §7erhalten.");
                    return;
                }
                return;
            }
            if (itemStack.getType() == Material.PAPER && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().startsWith("§7Gutschein§8: §e")) {
                String[] split = itemStack.getItemMeta().getDisplayName().split("§e");
                if (split.length == 2) {
                    String badgeName = split[1].replace("§8 (§7Badge§8)", "");
                    Badge badge = plugin.getBadgeManager().getBadge(badgeName);
                    if (badge == null) {
                        player.sendMessage(SkyMythPlugin.PREFIX + "§cDieses Badge existiert nicht.");
                        return;
                    }
                    player.getInventory().remove(itemStack);
                    badge.getOwners().add(player.getUniqueId());
                    plugin.getBadgeManager().saveBadge(badge);
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast das Badge §e" + badge.getName() + " §7erhalten.");
                    return;
                }
                return;
            }
            if (itemStack.getType() == Material.PAPER && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().startsWith("§7Gutschein§8: §e")) {
                String[] split = itemStack.getItemMeta().getDisplayName().split("§e");
                if (split.length == 2) {
                    Kit kit = plugin.getKitManager().getKitByName(split[1].replace(" §8(§eKit§8)", ""));
                    if (kit == null) {
                        player.sendMessage(SkyMythPlugin.PREFIX + "§cDieses Kit existiert nicht.");
                        return;
                    }
                    player.getInventory().remove(itemStack);
                    kit.giveToAsVoucher(user);
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast das Kit §e" + kit.getName() + " §7erhalten.");
                    return;
                }
                return;
            }
            itemStack.amount(itemStack.getAmount() - 1);
            if (itemStack.getAmount() == 0) {
                player.setItemInHand(new ItemStack(Material.AIR));
            } else {
                player.setItemInHand(itemStack);
            }
            return;
        }


        if (player.getWorld().getName().equalsIgnoreCase("Spawn")) {
            Block block = event.getClickedBlock();
            if (block == null) return;
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


        if(player.getWorld().getName().equalsIgnoreCase("world")) {
            Chunk chunk = event.getClickedBlock().getChunk();
            ProtectionManager protectionManager = plugin.getProtectorManager();
            Protector userProtector = protectionManager.getProtector(player.getUniqueId());
            if (protectionManager.isProtected(chunk) && userProtector.getProtectedChunks().stream().noneMatch(baseChunk -> baseChunk.getX() == chunk.getX() && baseChunk.getZ() == chunk.getZ())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(SkyMythPlugin.PREFIX + "§cDieses Chunk ist von " + plugin.getServer().getOfflinePlayer(protectionManager.getProtector(chunk).getOwner()).getName() + " geschützt.");
                return;
            }
            if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().startsWith("§8» §aBasisschutz")) {
                event.setCancelled(true);
                itemStack.amount(itemStack.getAmount() - 1);
                if (itemStack.getAmount() == 0) {
                    player.setItemInHand(new ItemStack(Material.AIR));
                } else {
                    player.setItemInHand(itemStack);
                }
                protectionManager.protect(userProtector, chunk);
                event.getPlayer().sendMessage(SkyMythPlugin.PREFIX + "§7Du hast den Chunk §e" + chunk.getX() + "§7;§e" + chunk.getZ() + " §7geschützt.");
                event.getPlayer().sendMessage(SkyMythPlugin.PREFIX + "§7Du kannst andere Spieler mit §e/bp invite <Spieler> §7einladen.");
            }
        }
    }

}
