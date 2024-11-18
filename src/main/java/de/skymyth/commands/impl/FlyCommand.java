package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class FlyCommand extends MythCommand {

    public FlyCommand(SkyMythPlugin plugin) {
        super("fly", "myth.fly", List.of("fliegen", "flymode", "flugmodus"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            World world = player.getWorld();
            if (!world.getName().equalsIgnoreCase("Spawn") && !player.hasPermission("myth.fly.anywhere")) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst am Spawn nicht fliegen.");
                return;
            }
            player.setAllowFlight(!player.getAllowFlight());
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Flugmodus: " + (player.getAllowFlight() ? "§aaktiviert" : "§cdeaktiviert"));
            return;
        }
        if (!player.hasPermission("myth.fly.other")) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDazu hast du keine Rechte.");
            return;
        }
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Spieler ist nicht online.");
            return;
        }
        World world = target.getWorld();
        if (!world.getName().equalsIgnoreCase("Spawn") && !target.hasPermission("myth.fly.anywhere")) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Spieler kann nur am Spawn fliegen.");
            return;
        }
        target.setAllowFlight(!target.getAllowFlight());
        target.sendMessage(SkyMythPlugin.PREFIX + "§7Flugmodus: " + (target.getAllowFlight() ? "§aaktiviert" : "§cdeaktiviert"));
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast den Flugmodus von §e" + target.getName() + " " + (target.getAllowFlight() ? "§aaktiviert" : "§cdeaktiviert"));
    }
}
