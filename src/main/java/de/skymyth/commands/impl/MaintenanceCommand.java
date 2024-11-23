package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class MaintenanceCommand extends MythCommand {

    public MaintenanceCommand(SkyMythPlugin plugin) {
        super("maintenance", "myth.maintenance", List.of("wartung"), plugin);
    }

    private void sendHelp(Player player) {
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /maintenance <on/off>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /maintenance <motd> <1/2> <text>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /maintenance <whitelist> <add/remove> <player>");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            sendHelp(player);
            return;
        }
        if (args[0].equalsIgnoreCase("on")) {
            plugin.getMaintenanceManager().enable();
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Wartungsmodus aktiviert.");
            for (UUID uuid : Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).filter(uniqueId -> !plugin.getMaintenanceManager().isWhitelisted(uniqueId)).toList()) {
                Bukkit.getPlayer(uuid).kickPlayer(plugin.getMaintenanceManager().getMaintenanceScreen());
            }
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Alle Spieler wurden gekickt.");
            return;
        }
        if (args[0].equalsIgnoreCase("off")) {
            plugin.getMaintenanceManager().disable();
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Wartungsmodus deaktiviert.");
            return;
        }
        if (args.length < 3) {
            sendHelp(player);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "motd":
                int line;
                try {
                    line = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sendHelp(player);
                    return;
                }
                if (line < 1 || line > 2) {
                    sendHelp(player);
                    return;
                }
                plugin.getMaintenanceManager().setMotdLine(line, String.join(" ", args).replaceFirst("motd " + line + " ", ""));
                player.sendMessage(SkyMythPlugin.PREFIX + "§7MOTD §8(§eZeile " + line + "§8) §7geändert.");
                break;
            case "whitelist":
                if (args[1].equalsIgnoreCase("add")) {
                    plugin.getMaintenanceManager().addWhitelist(player.getUniqueId());
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Du wurdest zur Whitelist hinzugefügt.");
                } else if (args[1].equalsIgnoreCase("remove")) {
                    plugin.getMaintenanceManager().removeWhitelist(player.getUniqueId());
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Du wurdest von der Whitelist entfernt.");
                } else {
                    sendHelp(player);
                }
                break;
            default:
                sendHelp(player);
        }
    }
}
