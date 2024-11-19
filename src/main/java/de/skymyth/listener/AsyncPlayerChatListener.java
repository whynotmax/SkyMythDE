package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.punish.model.result.PunishCheckResult;
import de.skymyth.punish.model.type.PunishType;
import de.skymyth.user.model.User;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

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

        String[] messageContent = message.split(" ");

        StringBuilder contentToSendToTeam = new StringBuilder();

        boolean foundAny = false;
        for (String content : messageContent) {
            boolean isBlocked = false;
            for (String blocked : blockedContent) {
                if (content.equalsIgnoreCase(blocked)) {
                    foundAny = true;
                    contentToSendToTeam.append("§c§n").append(content).append("§7 ");
                    isBlocked = true;
                    break;
                }
            }
            if (!isBlocked) {
                contentToSendToTeam.append(content).append(" ");
            }
        }

        if (plugin.isGlobalMute() && !player.hasPermission("myth.team")) {
            event.setCancelled(true);
            player.sendMessage(SkyMythPlugin.PREFIX + "§cWährend dem Globalmute kannst du nicht schreiben.");
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
            return;
        }


        if (foundAny) {
            event.setCancelled(true);
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDeine Nachricht wird nun von Teammitgliedern überprüft.");
            for (var teamMember : Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("myth.team")).toList()) {
                teamMember.sendMessage(SkyMythPlugin.PREFIX + "§c" + player.getName() + "§7 hat versucht, folgendes zu schreiben:");
                teamMember.sendMessage(SkyMythPlugin.PREFIX + "§7" + contentToSendToTeam);
                teamMember.playSound(teamMember.getLocation(), Sound.ORB_PICKUP, 1, 1);
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
        var badge = (!hasBadge ? null : plugin.getBadgeManager().getBadge(user.getSelectedBadge())); //Implemented badge system

        if (badge != null) {
            chatPrefix = "§8[§f" + badge.getCharacter() + "§8] §7";
        }

        if (canUseColors != null && canUseColors.equalsIgnoreCase("true")) {
            message = message.replace("&", "§").replace("§k", "&k").replace("§n", "&n").replace("§m", "&m");
        }
        format = "§r " + chatPrefix + "§7" + player.getName() + "§8 × §7" + message;

        event.setFormat(format);
    }
}
