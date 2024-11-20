package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class RedisCommand extends MythCommand {

    public RedisCommand(SkyMythPlugin plugin) {
        super("redis", "myth.redis", List.of("redisson"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Redisson verbunden§8?");
        player.sendMessage(SkyMythPlugin.PREFIX + (plugin.getRedissonClient().isShutdown() ? "§cNein" : "§aJa"));
    }
}
