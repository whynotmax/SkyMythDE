package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.TitleUtil;
import de.skymyth.utility.Util;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public record PlayerMoveListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (Util.FREEZE.contains(player)) {
            player.teleport(plugin.getLocationManager().getPosition("spawn").getLocation());

            TitleUtil.sendTitle(player, 0, 40, 20, "§b§lEINGEFROREN", "§8× §7Du wurdest eingefroren §8×");

            player.sendMessage("§r");
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du wurdest aufgrund von §cdeinem Verhalten §7eingefroren.");
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Bitte schalte jegliche verbotene Minecraft Modifikationen aus, um einen Bann zu verhindern.");
            player.sendMessage("§r");

            player.playSound(player.getLocation().add(0, 5, 0), Sound.IRONGOLEM_WALK, 1, 1);
        }

        if (player.getWorld().getName().equals("FpsArena")) {
            if (player.getLocation().distance(plugin.getLocationManager().getPosition("arena").getLocation()) > 100) {
                player.teleport(plugin.getLocationManager().getPosition("spawn").getLocation());
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu darfst die Arena nicht verlassen.");
                player.playSound(player.getLocation().add(0, 5, 0), Sound.IRONGOLEM_HIT, 1, 1);
            }
        }

        if (player.getWorld().getName().equals("Spawn")) {
            if (player.getLocation().distance(plugin.getLocationManager().getPosition("spawn").getLocation()) > 200) {
                player.teleport(plugin.getLocationManager().getPosition("spawn").getLocation());
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu darfst den Spawn nicht verlassen.");
                player.playSound(player.getLocation().add(0, 5, 0), Sound.IRONGOLEM_HIT, 1, 1);
            }
        }
    }

}
