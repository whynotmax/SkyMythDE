package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.rtp.RTPManager;
import de.skymyth.user.model.User;
import de.skymyth.user.model.cooldown.Cooldown;
import de.skymyth.utility.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class RandomTPCommand extends MythCommand {

    public RandomTPCommand(SkyMythPlugin plugin) {
        super("randomtp", null, List.of("rtp"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        if (user.isOnCooldown("randomTP")) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst noch " + TimeUtil.beautifyTime(user.getCooldown("randomTP").getRemainingTime(), TimeUnit.MILLISECONDS, true, true) + " warten.");
            return;
        }
        Cooldown cooldown = new Cooldown();
        cooldown.setName("randomTP");
        cooldown.setDuration(Duration.ofMinutes(1));
        cooldown.start();
        user.addCooldown(cooldown);

        player.sendMessage(SkyMythPlugin.PREFIX + "§7Es wird eine Stelle generiert...");

        CompletableFuture.supplyAsync(() -> {
            teleportPlayer(player, Bukkit.getWorld("world"));
            return null;
        });
    }

    public void teleportPlayer(Player player, World world) {

        int maxX = 2500;
        int maxZ = 2500;

        RTPManager rtpManager = new RTPManager(this.plugin, player, world, maxX, maxZ);
        rtpManager.teleport();

    }
}
