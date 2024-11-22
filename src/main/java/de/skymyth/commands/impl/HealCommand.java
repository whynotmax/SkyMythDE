package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.user.model.User;
import de.skymyth.user.model.cooldown.Cooldown;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;

public class HealCommand extends MythCommand {

    public HealCommand(SkyMythPlugin plugin) {
        super("heal", "myth.heal", List.of("pflaster", "heilung"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            User user = plugin.getUserManager().getUser(player.getUniqueId());
            if (user.isOnCooldown("command_heal")) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst diesen Befehl nur alle 30 Sekunden verwenden.");
                return;
            }

            Cooldown cooldown = new Cooldown();
            cooldown.setName("command_heal");
            cooldown.setDuration(Duration.ofSeconds(30));
            cooldown.start();
            user.addCooldown(cooldown);

            plugin.getUserManager().saveUser(user);

            player.setHealth(20);
            player.setFoodLevel(20);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du wurdest geheilt.");
            return;
        }
        if (!player.hasPermission("myth.heal.other")) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDazu hast du keine Rechte.");
            return;
        }
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Spieler ist nicht online.");
            return;
        }
        target.setHealth(20);
        target.setFoodLevel(20);
        target.sendMessage(SkyMythPlugin.PREFIX + "§7Du wurdest geheilt.");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + target.getName() + " §7geheilt.");
    }
}
