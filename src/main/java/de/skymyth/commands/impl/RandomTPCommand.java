package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.user.model.User;
import de.skymyth.user.model.cooldown.Cooldown;
import de.skymyth.utility.RandomUtil;
import de.skymyth.utility.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.List;
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

        RandomUtil.teleportRandomly(player, Bukkit.getWorld("world")); //TODO: Change to Farmworld
    }
}
