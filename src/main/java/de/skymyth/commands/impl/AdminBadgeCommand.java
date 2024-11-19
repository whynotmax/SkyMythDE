package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.badge.model.Badge;
import de.skymyth.commands.MythCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminBadgeCommand extends MythCommand {

    public AdminBadgeCommand(SkyMythPlugin plugin) {
        super("adminbadge", "myth.owner", List.of("abadge", "badgeadmin"), plugin);
    }

    public void sendHelp(Player player) {
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende:");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/abadge create <name> <char> <description>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/abadge setcolor <name> <color>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/abadge delete <name>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/abadge add <player> <badge>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/abadge remove <player> <badge>");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            this.sendHelp(player);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "create":
                if (args.length < 4) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /abadge create <name> <char> <description>");
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 3; i < args.length; i++) {
                    stringBuilder.append(args[i].replace('&', '§')).append(" ");
                }
                plugin.getBadgeManager().createBadge(new Badge(
                        args[1],
                        stringBuilder.toString().trim(),
                        "§f",
                        String.valueOf(args[2].charAt(0)),
                        System.currentTimeMillis(),
                        player.getUniqueId(),
                        new ArrayList<>()
                ));
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Badge §e" + args[1] + " §7erstellt.");
                break;
            case "setcolor":
                if (args.length < 3) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /abadge setcolor <name> <color>");
                    return;
                }
                Badge badge3 = plugin.getBadgeManager().getBadge(args[1]);
                if (badge3 == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDieses Badge existiert nicht.");
                    return;
                }
                badge3.setColor(args[2].replace('&', '§'));
                plugin.getBadgeManager().saveBadge(badge3);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Farbe von Badge §e" + args[1] + " §7zu §e" + args[2].replace("§", "&") + " §7geändert.");
                break;
            case "delete":
                if (args.length < 2) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /abadge delete <name>");
                    return;
                }
                Badge badge = plugin.getBadgeManager().getBadge(args[1]);
                if (badge == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDieses Badge existiert nicht.");
                    return;
                }
                plugin.getBadgeManager().deleteBadge(badge);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Badge §e" + args[1] + " §7gelöscht.");
                break;
            case "add":
                if (args.length < 3) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /abadge add <player> <badge>");
                    return;
                }
                Badge badge1 = plugin.getBadgeManager().getBadge(args[2]);
                if (badge1 == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDieses Badge existiert nicht.");
                    return;
                }
                OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(args[1]);
                if (offlinePlayer == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler existiert nicht.");
                    return;
                }
                badge1.getOwners().add(offlinePlayer.getUniqueId());
                plugin.getBadgeManager().saveBadge(badge1);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Badge §e" + args[2] + " §7zu §e" + offlinePlayer.getName() + "§7 hinzugefügt.");
                break;
            case "remove":
                if (args.length < 3) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /abadge remove <player> <badge>");
                    return;
                }
                Badge badge2 = plugin.getBadgeManager().getBadge(args[2]);
                if (badge2 == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDieses Badge existiert nicht.");
                    return;
                }
                OfflinePlayer offlinePlayer1 = plugin.getServer().getOfflinePlayer(args[1]);
                if (offlinePlayer1 == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler existiert nicht.");
                    return;
                }
                badge2.getOwners().remove(offlinePlayer1.getUniqueId());
                plugin.getBadgeManager().saveBadge(badge2);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Badge §e" + args[2] + " §7von §e" + offlinePlayer1.getName() + "§7 entfernt.");
                break;
            default:
                this.sendHelp(player);
                break;
        }
    }
}
