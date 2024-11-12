package de.skymyth;


import de.skymyth.listener.PlayerJoinListener;
import de.skymyth.scoreboard.ScoreboardManager;
import eu.koboo.en2do.Credentials;
import eu.koboo.en2do.MongoManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;

@Log
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SkyMythPlugin extends JavaPlugin {

    final String prefix = "§8» §5§lSkyMyth.DE §8| §7";

    SkyMythPlugin plugin;

    MongoManager mongoManager;
    ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        plugin = this;

        mongoManager = new MongoManager(Credentials.of("", ""));
        scoreboardManager = new ScoreboardManager(plugin);


        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(plugin), this);

        log.info("SkyMyth Plugin enabled.");
    }

    @Override
    public void onDisable() {
        log.info("SkyMyth Plugin disabled.");
    }
}
