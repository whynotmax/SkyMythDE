package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.punish.model.result.PunishCheckResult;
import de.skymyth.punish.model.type.PunishType;
import de.skymyth.user.model.User;
import de.skymyth.utility.TextComponentBuilder;
import de.skymyth.utility.Util;
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

        TextComponentBuilder newMessage = new TextComponentBuilder("");
        if (badge != null) {
            chatPrefix = "§8[§f" + badge.getColor() + badge.getCharacter() + "§8] §7";

            ItemBuilder badgeItem = new ItemBuilder(Material.PAPER);
            badgeItem.setName("§8[§e" + badge.getColor() + badge.getCharacter() + "§8] §7Badge");
            badgeItem.lore(
                    "§7§o" + badge.getDescription()
            );
            TextComponent hover = new TextComponent(chatPrefix);
            hover.setHoverEvent(Util.showItem(badgeItem));
            newMessage.append(hover);
        } else {
            newMessage.append(new TextComponent(chatPrefix));
        }

        if (canUseColors != null && canUseColors.equalsIgnoreCase("true")) {
            message = message.replace("&", "§").replace("§k", "&k").replace("§n", "&n").replace("§m", "&m");
        }

        TextComponentBuilder playerName = new TextComponentBuilder("§7" + player.getName());
        TextComponent hoverComponent = new TextComponentBuilder("§7Klicke, um seine Stats anzuzeigen.").toTextComponent();
        playerName.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/stats " + player.getName()));
        playerName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{hoverComponent}));

        newMessage.append(playerName);
        newMessage.append(new TextComponent("§8 × §7" + message));

        event.setCancelled(true);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.spigot().sendMessage(newMessage.toTextComponent());
        }
        //event.setFormat(format);
    }

}
