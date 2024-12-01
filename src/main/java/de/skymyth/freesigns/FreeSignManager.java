package de.skymyth.freesigns;

import de.skymyth.SkyMythPlugin;
import de.skymyth.freesigns.model.FreeSign;
import de.skymyth.freesigns.repository.FreeSignRepository;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FreeSignManager {

    SkyMythPlugin plugin;
    Map<Location, FreeSign> signs;
    FreeSignRepository repository;

    public FreeSignManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.repository = plugin.getMongoManager().create(FreeSignRepository.class);
        this.signs = repository.findAll().stream().collect(Collectors.toMap(FreeSign::getLocation, Function.identity()));
    }

    public FreeSign create(Location location) {
        FreeSign sign = new FreeSign(signs.size() + 1, new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ()), new ItemStack(Material.STONE));
        signs.put(location, sign);
        return sign;
    }

    public FreeSign get(Location location) {
        return signs.get(location);
    }

    public boolean isFreeSign(Block block) {
        return signs.containsKey(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ()));
    }

    public void save(FreeSign sign) {
        signs.put(sign.getLocation(), sign);
        repository.save(sign);
    }

}
