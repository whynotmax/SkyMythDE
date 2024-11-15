package de.skymyth.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.location.model.Position;
import de.skymyth.utility.TitleUtil;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class WarpInventory extends AbstractInventory {

    SkyMythPlugin plugin;

    public WarpInventory(SkyMythPlugin plugin) {
        super("Warps: Menü", 54);
        this.plugin = plugin;

        defaultInventory();

        setItem(22, new ItemBuilder(Material.NETHER_STAR).setName("§7Warp§8: §eSpawn").lore(
                "§7§oTeleportiere dich zum Spawn",
                "§7",
                "§aKlicke§8,§7 um dich zu teleportieren"
        ), (event -> {
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            Position spawn = this.plugin.getLocationManager().getPosition("spawn");
            if (spawn != null) {
                player.teleport(spawn.toBukkitLocation());
                TitleUtil.sendTitle(player, 0, 40, 20, "§a§lSpawn", "§8× §7Du wurdest teleportiert §8×");
                return;
            }
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Spawn wurde noch nicht gesetzt.");
        }));
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
