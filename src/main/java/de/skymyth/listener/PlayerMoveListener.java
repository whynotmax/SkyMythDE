package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.TitleUtil;
import de.skymyth.utility.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;

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
    }

}
