package de.skymyth;


import de.skymyth.badge.BadgeManager;
import de.skymyth.clan.ClanManager;
import de.skymyth.commands.MythCommand;
import de.skymyth.giveaway.GiveawayManager;
import de.skymyth.inventory.InventoryManager;
import de.skymyth.kit.KitManager;
import de.skymyth.location.LocationManager;
import de.skymyth.protector.ProtectionManager;
import de.skymyth.punish.PunishManager;
import de.skymyth.ranking.RankingManager;
import de.skymyth.rewards.RewardsManager;
import de.skymyth.scoreboard.ScoreboardManager;
import de.skymyth.stattrack.enchant.EnchantWrapper;
import de.skymyth.tablist.TablistManager;
import de.skymyth.user.UserManager;
import de.skymyth.utility.codec.ChunkCodec;
import de.skymyth.utility.codec.DurationCodec;
import de.skymyth.utility.codec.ItemStackCodec;
import de.skymyth.utility.codec.LocationCodec;
import eu.koboo.en2do.Credentials;
import eu.koboo.en2do.MongoManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.Field;

@Log
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SkyMythPlugin extends JavaPlugin {

    public static final String PREFIX = "§8» §5§lSkyMyth.DE §8┃ §7";
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
    ProtectionManager protectorManager;


    @Override
    public void onEnable() {
        plugin = this;

        this.mongoManager = new MongoManager(Credentials.of("mongodb://minerush:Rbrmf5aPMt9hqgx7BWjLkGe2U38w46Kv@87.106.178.7:27017/", "skymyth"));
        this.mongoManager = this.mongoManager.registerCodec(new ItemStackCodec()).registerCodec(new LocationCodec()).registerCodec(new DurationCodec())
                .registerCodec(new ChunkCodec());

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
        this.protectorManager = new ProtectionManager(plugin);

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


        log.info("SkyMyth Plugin enabled.");
    }

    @Override
    public void onDisable() {
        this.rankingManager.delete();
        log.info("SkyMyth Plugin disabled.");
    }
}
