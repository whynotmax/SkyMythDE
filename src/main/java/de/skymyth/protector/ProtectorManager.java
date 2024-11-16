package de.skymyth.protector;

import de.skymyth.SkyMythPlugin;
import de.skymyth.protector.model.Protector;
import de.skymyth.protector.repository.ProtectorRepository;
import de.skymyth.utility.Util;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
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

        if (protector != null) {
            for (Chunk chunk : protector.getChunks()) {
                if(Util.containBlock(chunk, event.getBlock())) {
                    event.setCancelled(true);
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDie Basis von " + Bukkit.getOfflinePlayer(protector.getOwner()).getName() + " §cist geschützt!");
                    player.sendMessage("lol");
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Protector protector = this.getProtector(event.getBlock().getLocation());

        if (protector != null) {
            for (Chunk chunk : protector.getChunks()) {
                if(Util.containBlock(chunk, event.getBlock())) {
                    event.setCancelled(true);
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDie Basis von " + Bukkit.getOfflinePlayer(protector.getOwner()).getName() + " §cist geschützt!");
                    player.sendMessage("lol");
                }
            }
        }
    }


    public void createProtector(UUID uuid, Location location) {
        Protector protector = new Protector();
        protector.setProtectorUniqueId(UUID.randomUUID());
        protector.setOwner(uuid);
        protector.setChunks(new ArrayList<>());
        protector.setLocation(location);

        Chunk chunk = location.getChunk();
        protector.getChunks().add(chunk);

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

    public Protector getProtector(Location location) {
        for (Protector value : this.protectorMap.values()) {
            if (value.getLocation().equals(location)) {
                return value;
            }
        }
        return null;
    }


}
