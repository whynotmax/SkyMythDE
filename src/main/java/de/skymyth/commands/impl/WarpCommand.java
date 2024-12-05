package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.location.model.Position;
import de.skymyth.ui.WarpInventory;
import de.skymyth.utility.TeleportUtil;
import de.skymyth.utility.TitleUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class WarpCommand extends MythCommand {

    public WarpCommand(SkyMythPlugin plugin) {
        super("warp", null, List.of("warps", "listwarps", "warplist"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            plugin.getInventoryManager().openInventory(player, new WarpInventory(plugin));
            return;
        }
        String warpName = args[0];
        if (plugin.getLocationManager().getPosition(warpName) == null || !plugin.getLocationManager().getPosition(warpName).isWarp()) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Warp §e" + warpName + " §cexistiert nicht.");
            return;
        }
        Position warp = plugin.getLocationManager().getPosition(warpName);
        TeleportUtil.createTeleportation(plugin, player, warp.toBukkitLocation(), "Arena");
        TitleUtil.sendTitle(player, 0, 40, 20, "§a§l" + warp.getName(), "§8× §7Du wurdest teleportiert §8×");
    }
}
