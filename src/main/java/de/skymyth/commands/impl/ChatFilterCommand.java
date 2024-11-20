package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.chatfilter.ChatFilterManager;
import de.skymyth.chatfilter.model.ChatFilterItem;
import de.skymyth.commands.MythCommand;
import de.skymyth.punish.model.reason.PunishReason;
import de.skymyth.punish.model.type.PunishType;
import de.skymyth.utility.TimeUtil;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatFilterCommand extends MythCommand {

    public ChatFilterCommand(SkyMythPlugin plugin) {
        super("chatfilter", "myth.chatfilter", List.of("filter"), plugin);
    }

    private void sendHelp(Player player) {
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /chatfilter add <Wort>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /chatfilter exactMatch <Wort>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /chatfilter ignoreCase <Wort>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /chatfilter replaceLeetSpeak <Wort>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /chatfilter autoMute <Wort>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /chatfilter setMuteReason <Wort> <Reason ID>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /chatfilter remove <Wort>");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length <= 1) {
            sendHelp(player);
            return;
        }
        switch (args[0]) {
            case "add" -> {
                String word = args[1];
                if (plugin.getChatFilterManager().isChatFilterItem(word)) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDas Wort ist bereits in der Liste.");
                    return;
                }
                ChatFilterItem chatFilterItem = new ChatFilterItem();
                chatFilterItem.setWord(word);
                chatFilterItem.setExactMatch(false);
                chatFilterItem.setIgnoreCase(true);
                chatFilterItem.setReplaceLeetSpeak(false);
                chatFilterItem.setAutoMute(false);
                plugin.getChatFilterManager().addChatFilterItem(chatFilterItem);
                player.sendMessage(SkyMythPlugin.PREFIX + "§aDas Wort wurde hinzugefügt.");
                break;
            }
            case "exactMatch" -> {
                String word = args[1];
                if (!plugin.getChatFilterManager().isChatFilterItem(word)) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDas Wort ist nicht in der Liste.");
                    return;
                }
                plugin.getChatFilterManager().updateChatFilterItem(word, chatFilterItem -> {
                    chatFilterItem.setExactMatch(!chatFilterItem.isExactMatch());
                    player.sendMessage(SkyMythPlugin.PREFIX + "§aDas Wort wurde aktualisiert.");
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Exact Match: " + (chatFilterItem.isExactMatch() ? "§aJa" : "§cNein"));
                });
                break;
            }
            case "ignoreCase" -> {
                String word = args[1];
                if (!plugin.getChatFilterManager().isChatFilterItem(word)) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDas Wort ist nicht in der Liste.");
                    return;
                }
                plugin.getChatFilterManager().updateChatFilterItem(word, chatFilterItem -> {
                    chatFilterItem.setIgnoreCase(!chatFilterItem.isIgnoreCase());
                    player.sendMessage(SkyMythPlugin.PREFIX + "§aDas Wort wurde aktualisiert.");
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Ignore Case: " + (chatFilterItem.isIgnoreCase() ? "§aJa" : "§cNein"));
                });
                break;
            }
            case "replaceLeetSpeak" -> {
                String word = args[1];
                if (!plugin.getChatFilterManager().isChatFilterItem(word)) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDas Wort ist nicht in der Liste.");
                    return;
                }
                plugin.getChatFilterManager().updateChatFilterItem(word, chatFilterItem -> {
                    chatFilterItem.setReplaceLeetSpeak(!chatFilterItem.isReplaceLeetSpeak());
                    player.sendMessage(SkyMythPlugin.PREFIX + "§aDas Wort wurde aktualisiert.");
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Replace Leet Speak: " + (chatFilterItem.isReplaceLeetSpeak() ? "§aJa" : "§cNein"));
                });
                break;
            }
            case "autoMute" -> {
                String word = args[1];
                if (!plugin.getChatFilterManager().isChatFilterItem(word)) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDas Wort ist nicht in der Liste.");
                    return;
                }
                plugin.getChatFilterManager().updateChatFilterItem(word, chatFilterItem -> {
                    chatFilterItem.setAutoMute(!chatFilterItem.isAutoMute());
                    player.sendMessage(SkyMythPlugin.PREFIX + "§aDas Wort wurde aktualisiert.");
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Auto Mute: " + (chatFilterItem.isAutoMute() ? "§aJa" : "§cNein"));
                });
                break;
            }
            case "setMuteReason" -> {
                String word = args[1];
                if (!plugin.getChatFilterManager().isChatFilterItem(word)) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDas Wort ist nicht in der Liste.");
                    return;
                }
                ChatFilterItem chatFilterItem = plugin.getChatFilterManager().getChatFilterItem(word);
                if (!chatFilterItem.isAutoMute()) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDas Wort hat kein Auto Mute.");
                    return;
                }
                try {
                    int reasonId = Integer.parseInt(args[2]);
                    List<PunishReason> muteReasons = PunishReason.getReasonsByType(PunishType.MUTE);
                    if (reasonId < 1 || reasonId > muteReasons.size()) {
                        player.sendMessage(SkyMythPlugin.PREFIX + "§cDie ID muss zwischen 1 und " + muteReasons.size() + " liegen.");
                        player.sendMessage(SkyMythPlugin.PREFIX + "§7Folgende ID's stehen zur Verfügung:");
                        int index = 1;
                        for (PunishReason reason : PunishReason.getReasonsByType(PunishType.BAN)) {
                            player.sendMessage("§8- #§e" + index++ + " §7" + reason.getName() + " §8(§7" + reason.getDescription() + "§8) [§c" + TimeUtil.beautifyTime(reason.getPunishDuration().toMillis(), TimeUnit.MILLISECONDS, true, true) + "§8]");
                        }
                        return;
                    }
                    PunishReason reason = muteReasons.get(reasonId - 1);
                    plugin.getChatFilterManager().updateChatFilterItem(word, chatFilterItem1 -> {
                        chatFilterItem1.setAutoMuteReason(reason);
                        player.sendMessage(SkyMythPlugin.PREFIX + "§aDas Wort wurde aktualisiert.");
                        player.sendMessage(SkyMythPlugin.PREFIX + "§7Auto Mute Reason: " + reason.getName() + "§8 [§c" + TimeUtil.beautifyTime(reason.getPunishDuration().toMillis(), TimeUnit.MILLISECONDS, true, true) + "§8]");
                    });
                } catch (NumberFormatException e) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDie ID muss eine Zahl sein.");
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Folgende ID's stehen zur Verfügung:");
                    int index = 1;
                    for (PunishReason reason : PunishReason.getReasonsByType(PunishType.BAN)) {
                        player.sendMessage("§8- #§e" + index++ + " §7" + reason.getName() + " §8(§7" + reason.getDescription() + "§8) [§c" + TimeUtil.beautifyTime(reason.getPunishDuration().toMillis(), TimeUnit.MILLISECONDS, true, true) + "§8]");
                    }
                }
                break;
            }
            case null, default -> sendHelp(player);
        }
    }
}
