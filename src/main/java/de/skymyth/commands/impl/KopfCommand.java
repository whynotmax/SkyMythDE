package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.user.model.User;
import de.skymyth.user.model.cooldown.Cooldown;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.item.SkullCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class KopfCommand extends MythCommand {

    public KopfCommand(SkyMythPlugin plugin) {
        super("kopf", "myth.kopf", List.of("head"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        if (user.isOnCooldown("commandKopf")) {
            long remaining = user.getCooldown("commandKopf").getRemainingTime();
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst noch " + TimeUtil.beautifyTime(remaining, TimeUnit.MILLISECONDS, true, true) + " warten, bevor du den Befehl erneut verwenden kannst.");
            return;
        }

        if (args.length == 0) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /kopf <Spieler>");
            return;
        }
        String targetName = args[0];

        Cooldown cooldown = new Cooldown();
        cooldown.setName("commandKopf");
        cooldown.setDuration(Duration.ofDays(1));
        cooldown.start();
        user.addCooldown(cooldown);

        plugin.getUserManager().saveUser(user);

        ItemStack skull = SkullCreator.itemFromName(targetName);
        player.getInventory().addItem(skull);
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast den Kopf von §e" + targetName + " §7erhalten.");
    }
}
