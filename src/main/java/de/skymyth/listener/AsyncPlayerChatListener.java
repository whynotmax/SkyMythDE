package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    private final String[] blockedContent = {".de", ".eu", "nigger", "nigga", "n1gg4", "n1gga", "n1gger", "n1gg3r", ".com", ".net", ".org", ".tk", "sieg", "heil", "miethe"};

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().replaceAll("%", "%%");

        String[] messageContent = message.split(" ");

        StringBuilder contentToSendToTeam = new StringBuilder();

        boolean foundAny = false;
        for (String content : messageContent) {
            for (String blocked : blockedContent) {
                if (content.equalsIgnoreCase(blocked)) {
                    foundAny = true;
                    contentToSendToTeam.append("§c§n").append(content).append("§7 ");
                    break;
                } else {
                    contentToSendToTeam.append(content).append(" ");
                }
            }
        }

        if (foundAny) {
            event.setCancelled(true);
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDeine Nachricht wird nun von Teammitgliedern überprüft.");
            for (var teamMember : Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("skymyth.team")).toList()) {
                teamMember.sendMessage(SkyMythPlugin.PREFIX + "§c" + player.getName() + "§7 hat versucht, folgendes zu schreiben:");
                teamMember.sendMessage(SkyMythPlugin.PREFIX + "§7" + contentToSendToTeam);
                teamMember.playSound(teamMember.getLocation(), Sound.ORB_PICKUP, 1, 1);
            }
            return;
        }

        String format;

        var playerGroup = LuckPermsProvider.get().getGroupManager().getGroup(LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getPrimaryGroup());
        var chatPrefix = playerGroup.getCachedData().getMetaData().getMetaValue("chat-prefix");
        if (chatPrefix == null) chatPrefix = "§r" + playerGroup.getName();
        else chatPrefix = chatPrefix.replace("&", "§");

        var badge = ""; //TODO: Implement badge system

        //noinspection ConstantValue
        if (!badge.isEmpty()) {
            chatPrefix = "§r §8[" + badge + "§8] §7";
        }

        format = "§r " + chatPrefix + "§7" + player.getName() + "§8 × §7" + message;

        event.setFormat(format);
    }
}
