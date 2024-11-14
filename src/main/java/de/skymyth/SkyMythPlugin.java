package de.skymyth;


import de.skymyth.clan.ClanManager;
import de.skymyth.commands.impl.*;
import de.skymyth.giveaway.GiveawayManager;
import de.skymyth.inventory.InventoryManager;
import de.skymyth.listener.AsyncPlayerChatListener;
import de.skymyth.listener.CombatListener;
import de.skymyth.listener.PlayerJoinListener;
import de.skymyth.listener.PlayerQuitListener;
import de.skymyth.location.LocationManager;
import de.skymyth.scoreboard.ScoreboardManager;
import de.skymyth.stattrack.enchant.EnchantWrapper;
import de.skymyth.tablist.TablistManager;
import de.skymyth.user.UserManager;
import de.skymyth.utility.codec.ItemStackCodec;
import de.skymyth.utility.codec.LocationCodec;
import eu.koboo.en2do.Credentials;
import eu.koboo.en2do.MongoManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

@Log
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SkyMythPlugin extends JavaPlugin {

    public static final String PREFIX = "§8» §5§lSkyMyth.DE §8┃ §7";

    SkyMythPlugin plugin;

    MongoManager mongoManager;
    ScoreboardManager scoreboardManager;
    UserManager userManager;
    TablistManager tablistManager;
    LocationManager locationManager;
    InventoryManager inventoryManager;
    GiveawayManager giveawayManager;
    ClanManager clanManager;

    @Override
    public void onEnable() {
        plugin = this;

        this.mongoManager = new MongoManager(Credentials.of("mongodb://minerush:Rbrmf5aPMt9hqgx7BWjLkGe2U38w46Kv@87.106.178.7:27017/", "skymyth"));
        this.mongoManager = this.mongoManager.registerCodec(new ItemStackCodec()).registerCodec(new LocationCodec());

        this.scoreboardManager = new ScoreboardManager(plugin);
        this.userManager = new UserManager(plugin);
        this.tablistManager = new TablistManager(plugin);
        this.locationManager = new LocationManager(plugin);
        this.inventoryManager = new InventoryManager(plugin);
        this.giveawayManager = new GiveawayManager(plugin);
        this.clanManager = new ClanManager(plugin);

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(plugin), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(plugin), this);
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new CombatListener(plugin), this);

        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register("msg", new MessageCommand(plugin));
            commandMap.register("r", new ReplyCommand(plugin));
            commandMap.register("pay", new PayCommand(plugin));
            commandMap.register("ping", new PingCommand(plugin));
            commandMap.register("chatclear", new ChatclearCommand(plugin));
            commandMap.register("test", new TestCommand(plugin));
            commandMap.register("team", new TeamCommand(plugin));
            commandMap.register("clan", new ClanCommand(plugin));
            commandMap.register("setloc", new SetlocCommand(plugin));

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);

            Enchantment.registerEnchantment(EnchantWrapper.STAT_TRACK);
        } catch (IllegalArgumentException | ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        log.info("SkyMyth Plugin enabled.");
    }

    @Override
    public void onDisable() {
        log.info("SkyMyth Plugin disabled.");
    }

    public Command findCommandByName(String name) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) bukkitCommandMap.get(Bukkit.getServer());

            Command closestCommand = null;
            int closestDistance = Integer.MAX_VALUE;

            for (String commandName : commandMap.getCommands().stream().map(Command::getName).toList()) {
                int distance = levenshteinDistance(name, commandName);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestCommand = commandMap.getCommand(commandName);
                }
            }

            return closestCommand;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1),
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }

        return dp[a.length()][b.length()];
    }
}
