package de.skymyth;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import de.skymyth.commands.impl.*;
import de.skymyth.crate.CrateManager;
import de.skymyth.inventory.InventoryManager;
import de.skymyth.listener.AsyncPlayerChatListener;
import de.skymyth.listener.PlayerJoinListener;
import de.skymyth.listener.PlayerQuitListener;
import de.skymyth.location.LocationManager;
import de.skymyth.scoreboard.ScoreboardManager;
import de.skymyth.tablist.TablistManager;
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
    TablistManager tablistManager;
    LocationManager locationManager;
    InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        plugin = this;

        this.mongoManager = new MongoManager(Credentials.of("mongodb://minerush:Rbrmf5aPMt9hqgx7BWjLkGe2U38w46Kv@87.106.178.7:27017/", "skymyth"));
        this.mongoManager.registerCodec(new ItemStackCodec()).registerCodec(new LocationCodec()).registerCodec(new CrateItemCodec());

        this.scoreboardManager = new ScoreboardManager(plugin);
        this.crateManager = new CrateManager(plugin);
        this.userManager = new UserManager(plugin);
        this.tablistManager = new TablistManager(plugin);
        this.locationManager = new LocationManager(plugin);
        this.inventoryManager = new InventoryManager(plugin);

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(plugin), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(plugin), this);
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatListener(), this);

        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register("msg", new MessageCommand(plugin));
            commandMap.register("r", new ReplyCommand(plugin));
            commandMap.register("pay", new PayCommand(plugin));
            commandMap.register("ping", new PingCommand(plugin));
            commandMap.register("chatclear", new ChatclearCommand(plugin));

            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Client.TAB_COMPLETE}) {
                public void onPacketReceiving(PacketEvent event) {
                    if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE)
                        try {
                            PacketContainer packet = event.getPacket();
                            String message = (packet.getSpecificModifier(String.class).read(0)).toLowerCase();
                            // The following is a boolean function that returns true if the command should be cancelled
                            //if (CommonScripts.commandIsNotInThree(event.getPlayer(), message)) {
                            // Cancel the event
                            event.setCancelled(true);
                            //}
                        } catch (FieldAccessException e) {
                            e.printStackTrace();
                        }
                }
            });
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        log.info("SkyMyth Plugin enabled.");
    }

    @Override
    public void onDisable() {
        log.info("SkyMyth Plugin disabled.");
    }
}
