package de.skymyth.utility;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.item.SkullCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class SkullLoader {

    SkyMythPlugin plugin;
    File file;
    YamlConfiguration yamlConfiguration;

    public SkullLoader(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "skulls.yml");
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void addSkull(UUID uuid) {
        this.yamlConfiguration.set("skull." + uuid.toString(), SkullCreator.itemFromUuid(uuid));
        try {
            this.yamlConfiguration.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ItemStack getSkull(UUID uuid) {
        if(this.yamlConfiguration.get("skull." + uuid.toString()) == null) {
            return SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDEzZTk2ZGY2ZWQ0YjcwYTVhNzBmYzI5ZGNkZTkzMTRkYmU5NzY2OTY0NzRmMTIwZTBiMzBlYTVkN2I5NmIzYSJ9fX0=");
        }
        return this.yamlConfiguration.getItemStack("skull." + uuid.toString());
    }
}
