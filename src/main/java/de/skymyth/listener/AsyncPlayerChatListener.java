package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.badge.model.Badge;
import de.skymyth.chatfilter.model.ChatFilterItem;
import de.skymyth.punish.model.Punish;
import de.skymyth.punish.model.reason.PunishReason;
import de.skymyth.punish.model.result.PunishCheckResult;
import de.skymyth.punish.model.type.PunishType;
import de.skymyth.user.model.User;
import de.skymyth.utility.TextComponentBuilder;
import de.skymyth.utility.item.ItemBuilder;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Pattern;

public class AsyncPlayerChatListener implements Listener {

    private final Pattern accentfilter = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    private final String[] blockedContent = {".de", ".eu", "nigger", "nigga", "n1gg4", "n1gga", "n1gger", "n1gg3r", ".com", ".net", ".org", ".tk", "sieg", "heil", "miethe"};
    SkyMythPlugin plugin;

    public AsyncPlayerChatListener(SkyMythPlugin plugin) {
        this.plugin = plugin;
    }

    private static TextComponent getHover(Badge badge, String chatPrefix) {
        ItemBuilder badgeItem = new ItemBuilder(Material.PAPER);
        badgeItem.setName("§8[§e" + badge.getColor() + badge.getCharacter() + "§8] §7Badge");
        badgeItem.lore(
                "§7§o" + badge.getDescription()
        );
        TextComponent hover = new TextComponent(chatPrefix);
        hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(badgeItem.getItemMeta().getDisplayName() + "\n" + badgeItem.getItemMeta().getLore().get(0))}));
        return hover;
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

        boolean containsBlockedContent = plugin.getChatFilterManager().checkMessage(message);
        if (containsBlockedContent) {
            event.setCancelled(true);
            boolean isPunished = false;
            for (ChatFilterItem chatFilterItem : plugin.getChatFilterManager().getBlockedContent(message)) {
                if (chatFilterItem.isAutoMute() && !isPunished) {
                    isPunished = true;
                    PunishReason punishReason = chatFilterItem.getAutoMuteReason();
                    plugin.getPunishManager().mute(player.getUniqueId(), punishReason);
                }
            }
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDeine Nachricht enthält blockierten Inhalt.");
            if (isPunished) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu wurdest automatisch gemutet.");
            }
            player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.hasPermission("myth.team")) {
                    continue;
                }
                onlinePlayer.sendMessage(SkyMythPlugin.PREFIX + "§cDer Spieler §e" + player.getName() + " §chat blockierten Inhalt geschrieben.");
                onlinePlayer.sendMessage(SkyMythPlugin.PREFIX + "§cNachricht: §7" + plugin.getChatFilterManager().replaceBlockedWords(message, "§c§n%word%§7"));
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
            }
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

        TextComponentBuilder newMessage = new TextComponentBuilder("");
        if (badge != null) {
            chatPrefix = "§8[§f" + badge.getColor() + badge.getCharacter() + "§8] §7";

            TextComponent hover = getHover(badge, chatPrefix);
            newMessage.append(hover);
        } else {
            newMessage.append(new TextComponent(chatPrefix));
        }

        if (canUseColors != null && canUseColors.equalsIgnoreCase("true")) {
            message = message.replace("&", "§").replace("§k", "&k").replace("§n", "&n").replace("§m", "&m");
        }

        TextComponentBuilder playerName = new TextComponentBuilder("§7" + player.getName());
        TextComponent hoverComponent = new TextComponentBuilder("§7Klicke, um " + player.getName() + " eine Direktnachricht zu senden.").toTextComponent();
        playerName.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + player.getName() + " "));
        playerName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{hoverComponent}));

        newMessage.append(playerName);
        newMessage.append(new TextComponent("§8 × §7" + message));

        event.setCancelled(true);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.spigot().sendMessage(newMessage.toTextComponent());
        }
    }

}
