package de.skymyth.redisson.verify;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.skymyth.SkyMythPlugin;
import de.skymyth.redisson.verify.model.DiscordVerifyMessage;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class VerifyManager {

    Cache<UUID, String> verificationCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).removalListener(removalNotification -> {
        if (!removalNotification.wasEvicted()) {
            return;
        }
        UUID uniqueId = (UUID) removalNotification.getKey();
        Player player = this.plugin.getServer().getPlayer(uniqueId);
        if (player != null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDein Verifizierungscode ist abgelaufen.");
            player.sendMessage(SkyMythPlugin.PREFIX + "§cBitte führe den Befehl erneut aus.");
        }
    }).build();
    SkyMythPlugin plugin;

    public VerifyManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
    }

    public void addVerification(UUID uniqueId, String verificationCode) {
        if (this.verificationCache.getIfPresent(uniqueId) != null) {
            return;
        }
        this.verificationCache.put(uniqueId, verificationCode);
        DiscordVerifyMessage discordVerifyMessage = new DiscordVerifyMessage();
        discordVerifyMessage.setUniqueId(uniqueId);
        discordVerifyMessage.setVerificationCode(verificationCode);
        discordVerifyMessage.setCompleted(false);
        discordVerifyMessage.setDiscordName(null);
        discordVerifyMessage.setDiscordId(0);
        this.plugin.getRedissonManager().getClient().getTopic("discord-verify").publish(discordVerifyMessage);
    }

    public String getVerificationCode(UUID uniqueId) {
        return this.verificationCache.getIfPresent(uniqueId);
    }

}
