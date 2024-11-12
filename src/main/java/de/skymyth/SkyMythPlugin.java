package de.skymyth;


import de.skymyth.listener.PlayerJoinListener;
import eu.koboo.en2do.Credentials;
import eu.koboo.en2do.MongoManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Log
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SkyMythPlugin extends JavaPlugin {

    String prefix = "§8» §5§lSkyMyth.DE §8| §7";

    MongoManager mongoManager;
    SkyMythPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        mongoManager = new MongoManager(Credentials.of("", ""));

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(plugin), this);

        log.info("SkyMyth Plugin enabled.");
    }

    @Override
    public void onDisable() {
        log.info("SkyMyth Plugin disabled.");
    }
}
