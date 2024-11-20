package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.user.model.User;
import de.skymyth.utility.UUIDFetcher;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class EcoCommand extends MythCommand {

    public EcoCommand(SkyMythPlugin plugin) {
        super("eco", null, List.of("economy", "tokens"), plugin);
    }

    private void sendHelp(Player player) {
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /eco <set/give/take/see> <Spieler> <Betrag>");
    }

    @Override
    public void run(Player player, String[] args) {
        if (!player.hasPermission("myth.eco")) {
            if (args.length == 0) {
                User user = this.plugin.getUserManager().getUser(player.getUniqueId());
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Dein Kontostand: §e" + NumberFormat.getInstance(Locale.GERMAN).format(user.getBalance()).replace(",", ".") + " Tokens");
                return;
            }
            String targetName = args[0];
            UUID targetUUID = UUIDFetcher.getUUID(targetName);
            if (targetUUID == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + targetName + " §7existiert nicht.");
                return;
            }
            User targetUser = this.plugin.getUserManager().getUser(targetUUID);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Kontostand von §e" + targetName + "§7: §e" + NumberFormat.getInstance(Locale.GERMAN).format(targetUser.getBalance()).replace(",", ".") + " Tokens");
            return;
        }
        if (args.length <= 1) {
            this.sendHelp(player);
            return;
        }
        String action = args[0];
        switch (action) {
            case "see" -> {
                String targetName = args[1];
                UUID targetUUID = UUIDFetcher.getUUID(targetName);
                if (targetUUID == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + targetName + " §7existiert nicht.");
                    return;
                }
                User targetUser = this.plugin.getUserManager().getUser(targetUUID);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Kontostand von §e" + targetName + "§7: §e" + NumberFormat.getInstance(Locale.GERMAN).format(targetUser.getBalance()).replace(",", ".") + " Tokens");
                break;
            }
            case "give" -> {
                String targetName = args[1];
                UUID targetUUID = UUIDFetcher.getUUID(targetName);
                if (targetUUID == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + targetName + " §7existiert nicht.");
                    return;
                }
                User targetUser = this.plugin.getUserManager().getUser(targetUUID);
                long amount;
                try {
                    amount = Long.parseLong(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Bitte gebe eine gültige Zahl an.");
                    return;
                }
                targetUser.addBalance(amount);
                this.plugin.getUserManager().saveUser(targetUser);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + targetName + " §7erfolgreich §e" + NumberFormat.getInstance(Locale.GERMAN).format(amount).replace(",", ".") + " Tokens §7gegeben.");
                break;
            }
            case "take" -> {
                String targetName = args[1];
                UUID targetUUID = UUIDFetcher.getUUID(targetName);
                if (targetUUID == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + targetName + " §7existiert nicht.");
                    return;
                }
                User targetUser = this.plugin.getUserManager().getUser(targetUUID);
                long amount;
                try {
                    amount = Long.parseLong(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Bitte gebe eine gültige Zahl an.");
                    return;
                }
                targetUser.removeBalance(amount);
                if (targetUser.getBalance() < 0) {
                    targetUser.setBalance(0);
                }
                this.plugin.getUserManager().saveUser(targetUser);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + targetName + " §7erfolgreich §e" + NumberFormat.getInstance(Locale.GERMAN).format(amount).replace(",", ".") + " Tokens §7genommen.");
                break;
            }
            case "set" -> {
                String targetName = args[1];
                UUID targetUUID = UUIDFetcher.getUUID(targetName);
                if (targetUUID == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + targetName + " §7existiert nicht.");
                    return;
                }
                User targetUser = this.plugin.getUserManager().getUser(targetUUID);
                long amount;
                try {
                    amount = Long.parseLong(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Bitte gebe eine gültige Zahl an.");
                    return;
                }
                targetUser.setBalance(amount);
                this.plugin.getUserManager().saveUser(targetUser);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast den Kontostand von §e" + targetName + " §7erfolgreich auf §e" + NumberFormat.getInstance(Locale.GERMAN).format(amount).replace(",", ".") + " Tokens §7gesetzt.");
                break;
            }
            default -> this.sendHelp(player);
        }
    }
}
