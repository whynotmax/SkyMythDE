package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.punish.model.result.PunishCheckResult;
import de.skymyth.punish.model.type.PunishType;
import de.skymyth.user.model.User;
import de.skymyth.user.model.cooldown.Cooldown;
import de.skymyth.utility.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WerbungCommand extends MythCommand {

    public WerbungCommand(SkyMythPlugin plugin) {
        super("werbung", "myth.werbung", List.of("advertise", "advert"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /werbung <Nachricht>");
            return;
        }
        User user = plugin.getUserManager().getUser(player.getUniqueId());

        PunishCheckResult result = plugin.getPunishManager().check(user.getUniqueId());
        if (result.isPunished() && result.getPunish().getType() == PunishType.MUTE) {
            plugin.getPunishManager().sendMuteMessage(result.getPunish());
            return;
        }

        if (user.hasCooldown("command_werbung")) {
            long remaining = user.getCooldown("command_werbung").getRemainingTime();
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst noch " + TimeUtil.beautifyTime(remaining, TimeUnit.MILLISECONDS, true, true) + " warten, bevor du den Befehl erneut verwenden kannst.");
            return;
        }

        Cooldown cooldown = new Cooldown();
        cooldown.setName("command_werbung");
        cooldown.setDuration(Duration.ofMinutes(15));
        cooldown.start();
        user.addCooldown(cooldown);

        plugin.getUserManager().saveUser(user);

        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg.replace("&", "§").replace("§k", "&k").replace("§n", "&n").replace("§m", "&m")).append(" ");
        }

        Bukkit.broadcastMessage("§8» §m--------------§8 × §6§lWERBUNG §8× §m--------------§8 «");
        Bukkit.broadcastMessage("§r");
        Bukkit.broadcastMessage("§8 × §7" + message);
        Bukkit.broadcastMessage("§8 × §7Spieler: §e" + player.getName());
        Bukkit.broadcastMessage("§r");
        Bukkit.broadcastMessage("§8» §m--------------§8 × §6§lWERBUNG §8× §m--------------§8 «");

        player.sendMessage(SkyMythPlugin.PREFIX + "§7Deine Werbung wurde gesendet.");
    }
}
