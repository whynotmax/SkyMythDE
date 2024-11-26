package de.skymyth;


import de.skymyth.advent.CalenderManager;
import de.skymyth.auctionhouse.AuctionHouseManager;
import de.skymyth.badge.BadgeManager;
import de.skymyth.baseprotector.BaseProtectorManager;
import de.skymyth.casino.CasinoManager;
import de.skymyth.chatfilter.ChatFilterManager;
import de.skymyth.clan.ClanManager;
import de.skymyth.commands.MythCommand;
import de.skymyth.commands.impl.console.WGKickCommand;
import de.skymyth.giveaway.GiveawayManager;
import de.skymyth.inventory.InventoryManager;
import de.skymyth.kit.KitManager;
import de.skymyth.listener.CombatListener;
import de.skymyth.location.LocationManager;
import de.skymyth.maintenance.MaintenanceManager;
import de.skymyth.motd.MOTDManager;
import de.skymyth.punish.PunishManager;
import de.skymyth.pvp.PvPShopManager;
import de.skymyth.ranking.RankingManager;
import de.skymyth.redisson.RedissonManager;
import de.skymyth.rewards.RewardsManager;
import de.skymyth.runnables.AntiLagRunnable;
import de.skymyth.runnables.TrophyRemovalRunnable;
import de.skymyth.scoreboard.ScoreboardManager;
import de.skymyth.tablist.TablistManager;
import de.skymyth.user.UserManager;
import de.skymyth.utility.SkullLoader;
import de.skymyth.utility.Util;
import de.skymyth.utility.codec.*;
import eu.koboo.en2do.Credentials;
import eu.koboo.en2do.MongoManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Log
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SkyMythPlugin extends JavaPlugin {

    public static final String PREFIX = "§8» §5§lSkyMyth.DE §8┃ §7";

    private final List<Location> randomPvPLocations = new ArrayList<>();

    SkyMythPlugin plugin;
    MongoManager mongoManager;
    ScoreboardManager scoreboardManager;
    UserManager userManager;
    TablistManager tablistManager;
    LocationManager locationManager;
    InventoryManager inventoryManager;
    GiveawayManager giveawayManager;
    ClanManager clanManager;
    PunishManager punishManager;
    BadgeManager badgeManager;
    RankingManager rankingManager;
    RewardsManager rewardsManager;
    KitManager kitManager;
    CasinoManager casinoManager;
    CombatListener combatListener;
    PvPShopManager pvPShopManager;
    AuctionHouseManager auctionHouseManager;
    BaseProtectorManager baseProtectorManager;
    ChatFilterManager chatFilterManager;
    CalenderManager calenderManager;
    SkullLoader skullLoader;
    RedissonManager redissonManager;
    MaintenanceManager maintenanceManager;
    MOTDManager motdManager;

    RedissonClient redissonClient;
    RMap<String, Integer> playerCount;

    @Override
    public void onEnable() {
        plugin = this;

        this.mongoManager = new MongoManager(Credentials.of("mongodb://minerush:Rbrmf5aPMt9hqgx7BWjLkGe2U38w46Kv@87.106.178.7:27017/", "skymyth"));
        this.mongoManager = this.mongoManager.registerCodec(new ItemStackCodec()).registerCodec(new LocationCodec()).registerCodec(new DurationCodec())
                .registerCodec(new ChunkCodec()).registerCodec(new CooldownCodec());

        Config redissonConfig = new Config();
        redissonConfig.useSingleServer().setAddress("redis://87.106.178.7:6379");
        redissonConfig.useSingleServer().setPassword("WsTvD7x8eMtAyjKE");

        this.redissonClient = Redisson.create(redissonConfig);
        this.playerCount = this.redissonClient.getMap("player-count");

        this.scoreboardManager = new ScoreboardManager(plugin);
        this.userManager = new UserManager(plugin);
        this.tablistManager = new TablistManager(plugin);
        this.locationManager = new LocationManager(plugin);
        this.inventoryManager = new InventoryManager(plugin);
        this.giveawayManager = new GiveawayManager(plugin);
        this.clanManager = new ClanManager(plugin);
        this.punishManager = new PunishManager(plugin);
        this.badgeManager = new BadgeManager(plugin);
        this.rankingManager = new RankingManager(plugin);
        this.rewardsManager = new RewardsManager(plugin);
        this.kitManager = new KitManager(plugin);
        this.casinoManager = new CasinoManager(plugin);
        this.pvPShopManager = new PvPShopManager(plugin);
        this.auctionHouseManager = new AuctionHouseManager(plugin);
        this.baseProtectorManager = new BaseProtectorManager(plugin);
        this.chatFilterManager = new ChatFilterManager(plugin);
        this.calenderManager = new CalenderManager(plugin);
        this.combatListener = new CombatListener(plugin);
        this.skullLoader = new SkullLoader(plugin);
        this.redissonManager = new RedissonManager(plugin, this.redissonClient);
        this.maintenanceManager = new MaintenanceManager(plugin);
        this.motdManager = new MOTDManager(plugin);

        Reflections listenerReflections = new Reflections("de.skymyth.listener");
        listenerReflections.getSubTypesOf(Listener.class).forEach(listener -> {
            try {
                Bukkit.getPluginManager().registerEvents(listener.getDeclaredConstructor(SkyMythPlugin.class).newInstance(plugin), plugin);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register("skymyth", new WGKickCommand());

            Reflections commandReflections = new Reflections("de.skymyth.commands.impl");
            commandReflections.getSubTypesOf(MythCommand.class).forEach(command -> {
                try {
                    commandMap.register("skymyth", command.getDeclaredConstructor(SkyMythPlugin.class).newInstance(plugin));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });


        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);

//            Enchantment.registerEnchantment(EnchantWrapper.STAT_TRACK);
        } catch (IllegalArgumentException | ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        for (int i = 1; i < 7; i++) {
            this.randomPvPLocations.add(this.locationManager.getPosition("pvp-random-" + i).getLocation());
        }

        Bukkit.getScheduler().runTaskTimer(this, new AntiLagRunnable(), 20L, 20L);
        Bukkit.getScheduler().runTaskTimer(this, new TrophyRemovalRunnable(plugin), 20L, 20 * 7L);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            this.playerCount.fastPut("skypvp", (Bukkit.getOnlinePlayers().size() - Util.VANISH.size()));
        }, 0L, 20 * 30L);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        log.info("SkyMyth Plugin enabled.");
    }

    @Override
    public void onDisable() {
        this.rankingManager.delete();
        this.playerCount.fastPut("skypvp", 0);
        log.info("SkyMyth Plugin disabled.");
    }
}
