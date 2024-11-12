package de.skymyth;


import de.skymyth.command.CrateCommand;
import de.skymyth.crate.CrateManager;
import de.skymyth.listener.PlayerJoinListener;
import de.skymyth.scoreboard.ScoreboardManager;
import de.skymyth.utility.codec.ItemStackCodec;
import de.skymyth.utility.codec.LocationCodec;
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

    final String prefix = "§8» §5§lSkyMyth.DE §8┃ §7";

    SkyMythPlugin plugin;

    MongoManager mongoManager;
    ScoreboardManager scoreboardManager;
    CrateManager crateManager;

    @Override
    public void onEnable() {
        plugin = this;

        this.mongoManager = new MongoManager(Credentials.of("mongodb://localhost:27017/", "skymyth"));
        this.mongoManager.registerCodec(new ItemStackCodec());
        this.mongoManager.registerCodec(new LocationCodec());

        this.scoreboardManager = new ScoreboardManager(plugin);
        this.crateManager = new CrateManager(plugin);


        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(plugin), this);

        this.getCommand("crate").setExecutor(new CrateCommand(plugin));

        log.info("SkyMyth Plugin enabled.");
    }

    @Override
    public void onDisable() {
        log.info("SkyMyth Plugin disabled.");
    }
}
