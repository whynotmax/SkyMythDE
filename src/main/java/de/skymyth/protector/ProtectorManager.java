package de.skymyth.protector;

import de.skymyth.SkyMythPlugin;
import de.skymyth.protector.model.Protector;
import de.skymyth.protector.repository.ProtectorRepository;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProtectorManager implements Listener {

    SkyMythPlugin plugin;
    ProtectorRepository repository;
    Map<UUID, Protector> protectorMap;

    public ProtectorManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.repository = plugin.getMongoManager().create(ProtectorRepository.class);
        this.protectorMap = new HashMap<>();
        this.repository.findAll().forEach(protector -> this.protectorMap.put(protector.getOwner(), protector));
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Protector protector = this.getProtector(event.getBlock().getLocation());

        if(protector != null) {
            if(this.isBlockInsideOfProtectedRadius(event.getBlock(), protector.getLocation(), Math.toIntExact(protector.getRadius())) && !protector.getOwner().equals(player.getUniqueId())) {
                event.setCancelled(true);
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDie Basis von " + Bukkit.getOfflinePlayer(protector.getOwner()).getName() + " §cist geschützt!");
                return;
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Protector protector = this.getProtector(event.getBlock().getLocation());

        if(protector != null) {
            if(this.isBlockInsideOfProtectedRadius(event.getBlock(), protector.getLocation(), Math.toIntExact(protector.getRadius())) && !protector.getOwner().equals(player.getUniqueId())) {
                event.setCancelled(true);
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDie Basis von " + Bukkit.getOfflinePlayer(protector.getOwner()).getName() + " §cist geschützt!");
                return;
            }
        }
    }

    public void createProtector(UUID uuid, Location location) {

        Protector protector = new Protector();
        protector.setProtectorUniqueId(UUID.randomUUID());
        protector.setOwner(uuid);
        protector.setRadius(5);
        protector.setMaxRadius(5);
        protector.setLocation(location);

        this.protectorMap.put(uuid, protector);
        this.repository.save(protector);
    }

    public ItemStack getProtectorItem() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.ENDER_PORTAL_FRAME)
                .setName("§8» §aBasisschutz")
                .lore(
                        "",
                        "§7Platziere diesen Basisschutz, um deine Basis einzugrenzen",
                        "§7der Standardradius beträgt 5- Blöcke.",
                        "",
                        "§7Der Radius kann später erweitert werden.",
                        "",
                        "§c§nPlatziere deinen Basisschutz mit Vorsicht!",
                        ""
                );

        return itemBuilder;
    }

    public boolean hasProtector(UUID uuid) {
        return this.protectorMap.get(uuid) != null;
    }

    public Protector getProtector(UUID uuid) {
        return this.protectorMap.get(uuid);
    }

    public boolean isInsideOfProtector(Player player) {
        for (Protector value : this.protectorMap.values()) {
            return player.getLocation().distance(value.getLocation()) < value.getRadius();
        }
        return false;
    }

    public boolean isBlockInsideOfProtectedRadius(Block block, Location protectorLocation, int radius) {
        return block.getLocation().distance(protectorLocation) < radius;
    }




    public Protector getProtector(Location location) {
        for (Protector value : this.protectorMap.values()) {
            if(value.getLocation().equals(location)) {
                return value;
            }
        }
        return null;
    }


}
