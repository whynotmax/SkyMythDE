package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.punish.model.result.PunishCheckResult;
import de.skymyth.punish.model.type.PunishType;
import de.skymyth.user.model.User;
import de.skymyth.utility.Util;
import de.skymyth.utility.item.ItemBuilder;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class AsyncPlayerChatListener implements Listener {

    private final Pattern accentfilter = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    private final String[] blockedContent = {".de", ".eu", "nigger", "nigga", "n1gg4", "n1gga", "n1gger", "n1gg3r", ".com", ".net", ".org", ".tk", "sieg", "heil", "miethe"};
    SkyMythPlugin plugin;

    public AsyncPlayerChatListener(SkyMythPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().replaceAll("%", "%%");

        PunishCheckResult punishCheckResult = plugin.getPunishManager().check(player.getUniqueId());
        if (punishCheckResult.isPunished() && punishCheckResult.getPunish().getType() == PunishType.MUTE) {
            event.setCancelled(true);
            plugin.getPunishManager().sendMuteMessage(punishCheckResult.getPunish());
            return;
        }

        if (plugin.isGlobalMute() && !player.hasPermission("myth.team")) {
            event.setCancelled(true);
            player.sendMessage(SkyMythPlugin.PREFIX + "§cWährend dem Globalmute kannst du nicht schreiben.");
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
            return;
        }


        User user = plugin.getUserManager().getUser(player.getUniqueId());

        String format;

        var playerGroup = LuckPermsProvider.get().getGroupManager().getGroup(LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getPrimaryGroup());
        var canUseColors = playerGroup.getCachedData().getMetaData().getMetaValue("chat-colors");
        var chatPrefix = playerGroup.getCachedData().getMetaData().getMetaValue("chat-prefix");
        if (chatPrefix == null) chatPrefix = "§r" + playerGroup.getName();
        else chatPrefix = chatPrefix.replace("&", "§");

        boolean hasBadge = user.getSelectedBadge() != null;
        var badge = (!hasBadge ? null : plugin.getBadgeManager().getBadge(user.getSelectedBadge()));

        TextComponent textComponent = new TextComponent("");
        if (badge != null) {
            chatPrefix = "§8[§f" + badge.getColor() + badge.getCharacter() + "§8] §7";

            ItemBuilder badgeItem = new ItemBuilder(Material.PAPER);
            badgeItem.setName("§8[§e" + badge.getColor() + badge.getCharacter() + "§8] §7Badge");
            badgeItem.lore(
                    "§8Badge-ID: " + badge.getName(),
                    "§r",
                    "§7§o" + badge.getDescription(),
                    "§r",
                    "§7Besitzer: §e" + badge.getOwners().size(),
                    "§7Erstellt um: §e" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(badge.getCreationDate()),
                    "§7Erstellt von: §e" + plugin.getServer().getOfflinePlayer(badge.getCreator()).getName(),
                    "§r",
                    (badge.getOwners().contains(player.getUniqueId()) ? "§a" : "§c")
                            + (badge.getOwners().contains(player.getUniqueId()) ? "Du besitzt dieses Badge." : "Du besitzt dieses Badge nicht.")
            );

            textComponent = new TextComponent(chatPrefix);
            TextComponent hover = new TextComponent(badge.getName());
            hover.setHoverEvent(Util.showItem(badgeItem));
            textComponent.addExtra(hover);
        }

        if (canUseColors != null && canUseColors.equalsIgnoreCase("true")) {
            message = message.replace("&", "§").replace("§k", "&k").replace("§n", "&n").replace("§m", "&m");
        }
        format = "§r " + textComponent + "§7" + player.getName() + "§8 × §7" + message;

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.spigot().sendMessage(new TextComponent(format));
        }
        //event.setFormat(format);
    }

}
