package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.TeleportUtil;
import de.skymyth.utility.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SpawnCommand extends MythCommand {


    public SpawnCommand(SkyMythPlugin plugin) {
        super("spawn", null, new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        if (args.length == 0) {
            TeleportUtil.createTeleportation(plugin, player, plugin.getLocationManager().getPosition("spawn").getLocation(), "Spawn");
            return;
        }
        if (args.length == 1 && player.hasPermission("myth.spawn.other")) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler wurde nicht gefunden.");
                return;
            }
            target.teleport(plugin.getLocationManager().getPosition("spawn").getLocation());
            TitleUtil.sendTitle(target, 0, 40, 20, "§a§lSpawn", "§8× §7Du wurdest teleportiert §8×");
            target.playSound(target.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0f, 1.0f);
            player.sendMessage(SkyMythPlugin.PREFIX + "§e" + target.getName() + " §7wurde zum §eSpawn §7teleportiert.");
            return;
        }

    }
}
