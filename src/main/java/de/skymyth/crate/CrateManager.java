package de.skymyth.crate;

import de.skymyth.SkyMythPlugin;
import de.skymyth.crate.inventory.CrateEditInventory;
import de.skymyth.crate.model.Crate;
import de.skymyth.crate.repository.CrateRepository;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CrateManager {

    SkyMythPlugin plugin;
    CrateRepository repository;

    public CrateManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.repository = this.plugin.getMongoManager().create(CrateRepository.class);
        Bukkit.getPluginManager().registerEvents(new CrateEditInventory(plugin), plugin);
    }

    public Crate createCrate(String name) {
        Crate crate = this.repository.findFirstById(name);

        if(crate == null) {
            crate = new Crate();
            crate.setName(name);
            crate.setDisplayName(name);
            crate.setCrateItems(new ArrayList<>());
            crate.setDisplayItem(new ItemStack(Material.DIRT));
            crate.setEnabled(true);
            this.repository.save(crate);
        } else {
            throw new RuntimeException("This crate name is already taken!");
        }
        return crate;
    }

    public Crate getCrate(String name) {
        return this.repository.findFirstById(name);
    }

    public boolean existsCrate(String name) {
        return getCrate(name) != null;
    }

    public void setDisplayItem(Crate crate, String base64) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.SKULL_ITEM)
                .setDataId(3)
                .addSkullValue(base64);
        itemBuilder.setName("§8» " + crate.getName() + " §7Crate §8× §eJetzt öffnen");
        crate.setDisplayItem(itemBuilder);
        this.repository.save(crate);
    }



}
