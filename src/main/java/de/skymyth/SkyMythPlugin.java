package de.skymyth;


import de.skymyth.command.CrateCommand;
import de.skymyth.crate.CrateManager;
import de.skymyth.listener.PlayerChatListener;
import de.skymyth.listener.PlayerJoinListener;
import de.skymyth.scoreboard.ScoreboardManager;
import de.skymyth.user.UserManager;
import de.skymyth.utility.codec.CrateItemCodec;
import de.skymyth.utility.codec.ItemStackCodec;
import de.skymyth.utility.codec.LocationCodec;
import eu.koboo.en2do.Credentials;
import eu.koboo.en2do.MongoManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;

import java.lang.reflect.Field;

@Log
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SkyMythPlugin extends JavaPlugin {

    public static final String PREFIX = "§8» §5§lSkyMyth.DE §8┃ §7";

    SkyMythPlugin plugin;

    MongoManager mongoManager;
    ScoreboardManager scoreboardManager;
    CrateManager crateManager;
    UserManager userManager;

    @Override
    public void onEnable() {
        plugin = this;

        this.mongoManager = new MongoManager(Credentials.of("mongodb://localhost:27017/", "skymyth"));
        this.mongoManager.registerCodec(new ItemStackCodec()).registerCodec(new LocationCodec()).registerCodec(new CrateItemCodec());

        this.scoreboardManager = new ScoreboardManager(plugin);
        this.crateManager = new CrateManager(plugin);
        this.userManager = new UserManager(plugin);


        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(plugin), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);

        //Commands
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            //Register commands
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        log.info("SkyMyth Plugin enabled.");
    }

    @Override
    public void onDisable() {
        log.info("SkyMyth Plugin disabled.");
    }

    public String getPrefix() {
        return PREFIX;
    }
}
