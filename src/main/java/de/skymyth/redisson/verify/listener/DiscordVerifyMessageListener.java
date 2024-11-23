package de.skymyth.redisson.verify.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;
import dev.mzcy.DiscordVerifyMessage;
import org.bukkit.entity.Player;
import org.redisson.api.listener.MessageListener;

import java.util.UUID;

public record DiscordVerifyMessageListener(SkyMythPlugin plugin) implements MessageListener<DiscordVerifyMessage> {

    @Override
    public void onMessage(CharSequence channel, DiscordVerifyMessage msg) {
        UUID uniqueId = msg.getUniqueId();
        boolean completed = msg.isCompleted();
        String discordName = msg.getDiscordName();
        long discordId = msg.getDiscordId();

        if (!completed) {
            return;
        }

        User user = plugin.getUserManager().getUser(uniqueId);
        user.setDiscordId(discordId);
        plugin.getUserManager().saveUser(user);

        Player player = plugin.getServer().getPlayer(uniqueId);

        if (player != null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du wurdest erfolgreich mit deinem Discord Account verifiziert.");
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Account: §e" + discordName);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Danke für deine Verifizierung!");
        }
    }

}
