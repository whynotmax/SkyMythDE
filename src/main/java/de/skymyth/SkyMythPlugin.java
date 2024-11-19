package de.skymyth;


import de.skymyth.auctionhouse.AuctionHouseManager;
import de.skymyth.badge.BadgeManager;
import de.skymyth.casino.CasinoManager;
import de.skymyth.clan.ClanManager;
import de.skymyth.listener.CombatListener;
import de.skymyth.commands.MythCommand;
import de.skymyth.giveaway.GiveawayManager;
import de.skymyth.inventory.InventoryManager;
import de.skymyth.kit.KitManager;
import de.skymyth.location.LocationManager;
import de.skymyth.punish.PunishManager;
import de.skymyth.pvp.PvPShopManager;
import de.skymyth.ranking.RankingManager;
import de.skymyth.rewards.RewardsManager;
import de.skymyth.runnables.AntiLagRunnable;
import de.skymyth.scoreboard.ScoreboardManager;
import de.skymyth.stattrack.enchant.EnchantWrapper;
import de.skymyth.tablist.TablistManager;
import de.skymyth.user.UserManager;
import de.skymyth.utility.codec.*;
import eu.koboo.en2do.Credentials;
import eu.koboo.en2do.MongoManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Log
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SkyMythPlugin extends JavaPlugin {

    public static final String PREFIX = "§8» §5§lSkyMyth.DE §8┃ §7";
    private final List<Location> randomPvPLocations = new ArrayList<>();
    private final List<String> allowedPlayers = new ArrayList<>();
    @Setter
    private boolean globalMute = false;
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

    private final ForkJoinPool forkJoinPool = new ForkJoinPool(5,
            ForkJoinPool.defaultForkJoinWorkerThreadFactory,
            (t, e) -> e.printStackTrace(System.out), true);


    @Override
    public void onEnable() {
        plugin = this;

        this.mongoManager = new MongoManager(Credentials.of("mongodb://minerush:Rbrmf5aPMt9hqgx7BWjLkGe2U38w46Kv@87.106.178.7:27017/", "skymyth"));
        this.mongoManager = this.mongoManager.registerCodec(new ItemStackCodec()).registerCodec(new LocationCodec()).registerCodec(new DurationCodec())
                .registerCodec(new ChunkCodec()).registerCodec(new CooldownCodec());

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

        this.combatListener = new CombatListener(plugin);

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

            Enchantment.registerEnchantment(EnchantWrapper.STAT_TRACK);
        } catch (IllegalArgumentException | ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        for (int i = 1; i < 7; i++) {
            this.randomPvPLocations.add(this.locationManager.getPosition("pvp-random-" + i).getLocation());
        }

        this.allowedPlayers.add("sxbide");
        this.allowedPlayers.add("044mzcy_og");
        this.allowedPlayers.add("Tony782");
        this.allowedPlayers.add("Lele_Mennels");

        Bukkit.getScheduler().runTaskTimer(this, new AntiLagRunnable(), 20L, 20L);

        log.info("SkyMyth Plugin enabled.");
    }

    @Override
    public void onDisable() {
        this.rankingManager.delete();
        log.info("SkyMyth Plugin disabled.");
    }

    private void setupNpcs() {

    }
}
