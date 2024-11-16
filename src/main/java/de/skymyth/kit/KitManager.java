package de.skymyth.kit;

import de.skymyth.SkyMythPlugin;
import de.skymyth.kit.model.Kit;
import de.skymyth.kit.model.type.KitType;
import de.skymyth.kit.repository.KitRepository;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class KitManager {

    SkyMythPlugin plugin;
    KitRepository repository;

    List<Kit> rankSpecificKits;
    List<Kit> otherKits;

    public KitManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.repository = plugin.getMongoManager().create(KitRepository.class);

        this.rankSpecificKits = new ArrayList<>(repository.findManyByType(KitType.RANK_SPECIFIC));
        this.otherKits = new ArrayList<>(repository.findAll().stream().filter(kit -> !kit.getType().equals(KitType.RANK_SPECIFIC)).toList());
    }

    public void createKit(String name, Material displayItem, KitType type, List<ItemStack> items) {
        if (this.getKitByName(name) != null) {
            throw new IllegalArgumentException("Kit with name " + name + " already exists.");
        }
        Kit kit = new Kit(name, displayItem, type, true, new ArrayList<>(), null, 0, null, items);
        repository.save(kit);

        if (type.equals(KitType.RANK_SPECIFIC)) {
            rankSpecificKits.add(kit);
        } else {
            otherKits.add(kit);
        }
    }

    public void deleteKit(String name) {
        Kit kit = this.getKitByName(name);
        repository.delete(kit);

        if (kit.getType().equals(KitType.RANK_SPECIFIC)) {
            rankSpecificKits.remove(kit);
        } else {
            otherKits.remove(kit);
        }
    }

    public Kit getKitByName(String name) {
        Kit kit;
        kit = rankSpecificKits.stream().filter(kit1 -> kit1.getName().equals(name)).findFirst().orElse(null);
        if (kit == null) {
            kit = otherKits.stream().filter(kit1 -> kit1.getName().equals(name)).findFirst().orElse(null);
        }
        if (kit == null) {
            kit = repository.findFirstById(name);
            if (kit != null) {
                if (kit.getType().equals(KitType.RANK_SPECIFIC)) {
                    rankSpecificKits.add(kit);
                } else {
                    otherKits.add(kit);
                }
            }
        }
        return kit;
    }

    public void saveKit(Kit kit) {
        if (Objects.requireNonNull(kit.getType()) == KitType.RANK_SPECIFIC) {
            rankSpecificKits.remove(getKitByName(kit.getName()));
            rankSpecificKits.add(kit);
        } else {
            otherKits.remove(getKitByName(kit.getName()));
            otherKits.add(kit);
        }
        repository.save(kit);
    }

}
