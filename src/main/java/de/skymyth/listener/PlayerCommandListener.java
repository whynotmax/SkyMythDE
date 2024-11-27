package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public record PlayerCommandListener(SkyMythPlugin plugin) implements Listener {


    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().toLowerCase();

        if (!command.contains("/msg")
                && !command.contains("/r")
                && !command.contains("/reply")) {

            for (Player onlinePlayers : Util.COMMANDWATCHER) {
                if (onlinePlayers == event.getPlayer()) continue;
                onlinePlayers.sendMessage(SkyMythPlugin.PREFIX + "§e" + event.getPlayer().getName() + " §8➟ §8(§f" + command + "§8)");
            }
        }

        if (!player.isOp()) {
            for (String blockedCommand : Util.BLOCKED_COMMANDS) {
                if (command.equalsIgnoreCase(blockedCommand) || command.split(" ")[0].contains(blockedCommand) && !command.equalsIgnoreCase("/playtime")) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDazu hast du keine Rechte.");
                    event.setCancelled(true);
                    System.out.println("[Blocker] " + player.getName() + " wanted to execute: " + command);
                    return;
                }
            }
        }

        if (plugin.getCombatListener().isInCombat(player)) {
            for (String blockedCommand : plugin.getCombatListener().blockedCommands) {
                if (command.equalsIgnoreCase(blockedCommand) || command.split(" ")[0].contains(blockedCommand) && !player.hasPermission("myth.team")) {
                    event.setCancelled(true);
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Befehl ist im Kampf nicht erlaubt.");
                }
            }
        }
    }
}
