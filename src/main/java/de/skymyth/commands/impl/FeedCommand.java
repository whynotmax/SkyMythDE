package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class FeedCommand extends MythCommand {

    public FeedCommand(SkyMythPlugin plugin) {
        super("feed", "myth.feed", List.of("essen"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            player.setFoodLevel(20);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast dich gesättigt.");
            return;
        }
        if (!player.hasPermission("myth.feed.other")) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDazu hast du keine Rechte.");
            return;
        }
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Spieler ist nicht online.");
            return;
        }
        target.setFoodLevel(20);
        target.sendMessage(SkyMythPlugin.PREFIX + "§7Du wurdest gesättigt.");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + target.getName() + " §7gesättigt.");
    }
}
