package de.skymyth.protector;

import de.skymyth.SkyMythPlugin;
import de.skymyth.protector.model.Protector;
import de.skymyth.protector.model.chunk.BaseChunk;
import de.skymyth.protector.repository.ProtectorRepository;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProtectionManager {

    SkyMythPlugin plugin;
    ProtectorRepository protectorRepository;

    Map<UUID, Protector> protectors;

    public ProtectionManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.protectorRepository = plugin.getMongoManager().create(ProtectorRepository.class);
        this.protectors = protectorRepository.findAll().stream().collect(Collectors.toMap(Protector::getOwner, protector -> protector));
    }

    public Protector getProtector(UUID owner) {
        return protectors.get(owner);
    }

    public Protector getProtector(Chunk chunk) {
        return protectors.values().stream().filter(protector -> {
            if (protector.getProtectedChunks() == null) {
                protector.setProtectedChunks(new ArrayList<>());
                protectorRepository.save(protector);
            }
            return protector.getProtectedChunks().stream()
                    .anyMatch(baseChunk -> baseChunk.getWorld().equals(chunk.getWorld().getName()) && baseChunk.getX() ==
                            chunk.getX() && baseChunk.getZ() == chunk.getZ());
        }).findFirst().orElse(null);
    }

    public boolean isProtected(Chunk chunk) {
        return protectors.values().stream().anyMatch(protector -> {
            if (protector.getProtectedChunks() == null) {
                protector.setProtectedChunks(new ArrayList<>());
                protectorRepository.save(protector);
            }
            return protector.getProtectedChunks().stream()
                    .anyMatch(baseChunk -> baseChunk.getWorld().equals(chunk.getWorld().getName()) && baseChunk.getX() ==
                            chunk.getX() && baseChunk.getZ() == chunk.getZ());
        });
    }

    public void protect(Protector protector, Chunk chunk) {
        protector.getProtectedChunks().add(new BaseChunk(chunk.getX(), chunk.getZ(), chunk.getWorld().getName()));
        protectorRepository.save(protector);
        protectors.put(protector.getOwner(), protector);
        System.out.println(3);
    }

    public List<BaseChunk> getProtectedChunks(UUID owner) {
        return protectors.get(owner).getProtectedChunks();
    }

    public void unprotect(Protector protector, Chunk chunk) {
        protector.getProtectedChunks().removeIf(baseChunk -> baseChunk.getWorld().equals(chunk.getWorld().getName()) && baseChunk.getX() ==
                chunk.getX() && baseChunk.getZ() == chunk.getZ());
        protectorRepository.save(protector);
        protectors.put(protector.getOwner(), protector);
    }

    public void addTrustedPlayer(Protector protector, UUID trustedPlayer) {
        protector.getTrustedPlayers().add(trustedPlayer);
        protectorRepository.save(protector);
        protectors.put(protector.getOwner(), protector);
    }

    public void removeTrustedPlayer(Protector protector, UUID trustedPlayer) {
        protector.getTrustedPlayers().remove(trustedPlayer);
        protectorRepository.save(protector);
        protectors.put(protector.getOwner(), protector);
    }

    public List<UUID> getTrustedPlayers(UUID owner) {
        return protectors.get(owner).getTrustedPlayers();
    }

    public void createProtector(UUID owner) {
        Protector protector = new Protector(owner, new ArrayList<>(), new ArrayList<>());
        protectorRepository.save(protector);
        protectors.put(owner, protector);
    }

    public ItemStack getProtectorItem() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.ENDER_PORTAL_FRAME);
        itemBuilder.setName("§8» §aBasisschutz");
        itemBuilder.lore(
                "§7Platziere diesen Block, um",
                "§7dein Grundstück zu schützen.",
                "",
                "§8» §aRechtsklick §8| §7Platziere den Block"
        );
        return itemBuilder;
    }

}
