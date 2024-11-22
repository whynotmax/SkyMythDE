package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.user.model.User;
import de.skymyth.user.model.cooldown.Cooldown;
import de.skymyth.utility.TimeUtil;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SaphirRewardCommand extends MythCommand {

    public SaphirRewardCommand(SkyMythPlugin plugin) {
        super("saphirreward", "myth.saphirreward", List.of(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        if (user.isOnCooldown("command_saphirReward")) {
            long remaining = user.getCooldown("command_saphirReward").getRemainingTime();
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst noch " + TimeUtil.beautifyTime(remaining, TimeUnit.MILLISECONDS, true, true) + " warten, bevor du den Befehl erneut verwenden kannst.");
            return;
        }
        Cooldown cooldown = new Cooldown();
        cooldown.setName("command_saphirReward");
        cooldown.setDuration(Duration.ofDays(1));
        cooldown.start();
        user.addCooldown(cooldown);

        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast deinen täglichen Saphir-Reward erhalten.");
        player.sendMessage(SkyMythPlugin.PREFIX + "§8+§f1.000 Tokens");

        user.addBalance(1000);

        plugin.getUserManager().saveUser(user);

    }
}
