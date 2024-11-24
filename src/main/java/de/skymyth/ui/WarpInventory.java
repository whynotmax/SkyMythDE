package de.skymyth.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.location.model.Position;
import de.skymyth.utility.TeleportUtil;
import de.skymyth.utility.item.ItemBuilder;
import de.skymyth.utility.item.SkullCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class WarpInventory extends AbstractInventory {

    SkyMythPlugin plugin;

    public WarpInventory(SkyMythPlugin plugin) {
        super("Warps: Menü", 54);
        this.plugin = plugin;

        defaultInventory();

        setItem(29, new ItemBuilder(Material.DIAMOND_SWORD).setName("§7Warp§8: §eArena")
                .lore(
                        "§7§oTeleportiere dich zur Arena",
                        "§7",
                        "§7Klicke,§7 um dich zu teleportieren"
                ), (event -> {
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            player.closeInventory();
            Position spawn = this.plugin.getLocationManager().getPosition("arena");
            if (spawn != null) {
                TeleportUtil.createTeleportation(plugin, player, plugin.getLocationManager().getPosition("arena").getLocation(), "Arena");
                return;
            }
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Spawn wurde noch nicht gesetzt.");
        }));

        setItem(20, new ItemBuilder(Material.IRON_PICKAXE).setName("§7Warp§8: §eFarmwelt")
                .lore(
                        "§7§oTeleportiere dich zur Farmwelt",
                        "§7",
                        "§7Klicke,§7 um dich zu teleportieren"
                ), (event -> {
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            player.closeInventory();
            Position spawn = this.plugin.getLocationManager().getPosition("farmwelt");
            if (spawn != null) {
                TeleportUtil.createTeleportation(plugin, player, plugin.getLocationManager().getPosition("farmwelt").getLocation(), "Farmwelt");
                return;
            }
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Spawn wurde noch nicht gesetzt.");
        }));

        setItem(22, new ItemBuilder(Material.NETHER_STAR).setName("§7Warp§8: §eSpawn").lore(
                "§7§oTeleportiere dich zum Spawn",
                "§7",
                "§7Klicke,§7 um dich zu teleportieren"
        ), (event -> {
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            player.closeInventory();
            Position spawn = this.plugin.getLocationManager().getPosition("spawn");
            if (spawn != null) {
                TeleportUtil.createTeleportation(plugin, player, plugin.getLocationManager().getPosition("spawn").getLocation(), "Spawn");
                return;
            }
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Spawn wurde noch nicht gesetzt.");
        }));

        setItem(24, new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzBmY2FiODM0ZTNlN2MwZDZhZjVkYzU5NzJmN2I5YWFiOGM3MzI4NTAzNWJkMDA3NWVlZGI3NGYzNTJlMDg2In19fQ==")).setName("§7Warp§8: §eBelohnungen").lore(
                "§7§oTeleportiere dich zu den Belohnungen",
                "§7",
                "§7Klicke,§7 um dich zu teleportieren"
        ), event -> {
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            player.closeInventory();
            Position rewards = this.plugin.getLocationManager().getPosition("rewards");
            if (rewards != null) {
                TeleportUtil.createTeleportation(plugin, player, plugin.getLocationManager().getPosition("rewards").getLocation(), "Belohnungen");
                return;
            }
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDie Belohnungen wurden noch nicht gesetzt.");
        });
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
