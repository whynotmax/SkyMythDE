package de.skymyth.baseprotector;

import de.skymyth.SkyMythPlugin;
import de.skymyth.baseprotector.model.BaseProtector;
import de.skymyth.baseprotector.model.radius.BaseProtectorRadius;
import de.skymyth.baseprotector.repository.BaseProtectorRepository;
import de.skymyth.utility.item.ItemBuilder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BaseProtectorManager {

    SkyMythPlugin plugin;
    BaseProtectorRepository repository;
    Map<UUID, BaseProtector> baseProtector;

    public BaseProtectorManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.repository = this.plugin.getMongoManager().create(BaseProtectorRepository.class);
        this.baseProtector = repository.findAll().stream().collect(Collectors.toMap(BaseProtector::getBaseOwner, baseProtector -> baseProtector));
    }

    public boolean hasBaseProtection(UUID uuid) {
        return this.baseProtector.containsKey(uuid);
    }

    public void createBaseProtection(UUID uuid, Location location) {
        if (hasBaseProtection(uuid)) return;
        BaseProtector baseProtector = new BaseProtector();

        baseProtector.setUniqueId(UUID.randomUUID());
        baseProtector.setBaseOwner(uuid);
        baseProtector.setBaseProtectorLocation(location);
        baseProtector.setTrustedPlayers(new ArrayList<>());
        baseProtector.setBaseProtectorRadius(BaseProtectorRadius.RADIUS_5X5);
        baseProtector.setMaxTrustedPlayers(10);

        this.baseProtector.put(uuid, baseProtector);
        this.repository.save(baseProtector);
    }

    public void deleteBaseProtection(UUID uuid) {
        if (!hasBaseProtection(uuid)) return;

        BaseProtector baseProtector = this.getBaseProtector(uuid);

        this.baseProtector.remove(uuid);
        this.repository.delete(baseProtector);
    }

    public boolean existsBaseProtection(BaseProtector baseProtector) {
        return this.baseProtector.containsKey(baseProtector.getBaseOwner());
    }

    public void saveBaseProtection(BaseProtector baseProtector) {
        this.baseProtector.put(baseProtector.getBaseOwner(), baseProtector);
        this.repository.save(baseProtector);
    }

    public BaseProtector getBaseProtection(Block block) {
        for (BaseProtector base : this.baseProtector.values()) {
            if (base.getBaseProtectorLocation().distance(block.getLocation()) < base.getBaseProtectorRadius().getRadius()) {
                return base;
            }
        }
        return null;
    }

    public double getBaseProtectionDistance(Block block) {
        for (BaseProtector base : this.baseProtector.values()) {
            return base.getBaseProtectorLocation().distance(block.getLocation());
        }
        return 0.0D;
    }

    public boolean isBaseSpotFine(Location location) {
        if(!location.getWorld().getName().equals("world")) return false;

        for (BaseProtector value : this.baseProtector.values()) {
            if (location.distance(value.getBaseProtectorLocation()) > BaseProtectorRadius.RADIUS_50X50.getRadius() * 2) {
                return true;
            }
        }
        return false;
    }


    public boolean isBlockProtected(Block block) {
        if(!block.getWorld().getName().equals("world")) return false;

        for (BaseProtector base : this.baseProtector.values()) {
            if (base.getBaseProtectorLocation().distance(block.getLocation()) < base.getBaseProtectorRadius().getRadius()) {
                return true;
            }
        }
        return false;
    }

    public ItemStack getBaseProtectorItem() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.ENDER_PORTAL_FRAME);
        itemBuilder.setName("§cBasisschutz");
        itemBuilder.lore(
                "§r",
                "§7Platziere diesen Basisschutz, um in einem Umkreis von 5 Blöcken",
                "§7deine Base vor Raids und Angriffen zu schützen.",
                "§r",
                "§eRechtsklicke anschließend den Block um Optionen zu ändern."
        );
        return itemBuilder;
    }

    public BaseProtector getBaseProtector(UUID uuid) {
        return this.baseProtector.get(uuid);
    }
}
