package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.user.model.User;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.ArrayList;

public class PayCommand extends MythCommand {

    SkyMythPlugin plugin;

    public PayCommand(SkyMythPlugin plugin) {
        super("pay", null, new ArrayList<>(), plugin);
        this.plugin = plugin;
    }

    @Override
    public void run(Player player, String[] args) {


        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);


            if (target == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler wurde nicht gefunden.");
                return;
            }

            if (target == player) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst dir selber nichts überweisen.");
                return;
            }

            int amount = 0;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException exception) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cBitte gebe eine gültige Anzahl an.");
            }

            if (amount < 1 || amount > 1000000) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst nur beträge zwischen 1 - 1 Mio. überweisen.");
                return;
            }

            User user = this.plugin.getUserManager().getUser(player.getUniqueId());
            User targetUser = this.plugin.getUserManager().getUser(target.getUniqueId());

            if (user.getBalance() < amount) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDazu ist dein Kontostand zu niedrig.");
                return;
            }


            user.removeBalance(amount);
            targetUser.addBalance(amount);

            plugin.getUserManager().saveUser(user);
            plugin.getUserManager().saveUser(targetUser);

            player.sendMessage(SkyMythPlugin.PREFIX + "§eDu §7hast §e" + target.getName() + " §e" + NumberFormat.getInstance().format(amount) + " §7Tokens überwiesen.");
            player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
            target.sendMessage(SkyMythPlugin.PREFIX + "§e" + player.getName() + " §7hat §eDir §e" + NumberFormat.getInstance().format(amount) + " §7Tokens überwiesen.");
            target.playSound(target.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
            return;
        }

        player.sendMessage(SkyMythPlugin.PREFIX + "§cVerwende: /pay <spieler> <anzahl>");


    }
}
