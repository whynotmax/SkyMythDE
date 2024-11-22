package de.skymyth.redisson;

import de.skymyth.SkyMythPlugin;
import de.skymyth.redisson.verify.VerifyManager;
import de.skymyth.redisson.verify.listener.DiscordVerifyMessageListener;
import de.skymyth.redisson.verify.model.DiscordVerifyMessage;
import lombok.Getter;
import org.redisson.api.RedissonClient;

@Getter
public class RedissonManager {

    SkyMythPlugin plugin;
    RedissonClient client;

    VerifyManager verifyManager;

    public RedissonManager(SkyMythPlugin plugin, RedissonClient client) {
        this.plugin = plugin;
        this.client = client;

        this.verifyManager = new VerifyManager(this.plugin);
        this.client.getTopic("discord-verify").addListener(DiscordVerifyMessage.class, new DiscordVerifyMessageListener(this.plugin));

    }

}
