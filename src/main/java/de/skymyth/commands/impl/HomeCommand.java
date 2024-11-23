package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.ui.HomesInventory;
import de.skymyth.user.model.User;
import de.skymyth.user.model.home.Home;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomeCommand extends MythCommand {

    public HomeCommand(SkyMythPlugin plugin) {
        super("home", null, new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        User user = plugin.getUserManager().getUser(player.getUniqueId());
        // /home <set,delete> <name>
        if(args.length == 2 && args[0].equalsIgnoreCase("set")) {
            String homeName = args[1];

            if(homeName.length() < 2) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Name muss mindestens 2 Buchstaben haben.");
                return;
            }

            if(homeName.length() > 10) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Name darf max. 10 Buchstaben haben.");
                return;
            }

            if(user.getHomes().size() > 10) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst nicht mehr Homes als 10 setzen.");
                return;
            }

            Home home = new Home(homeName, System.currentTimeMillis(), player.getLocation());
            user.getHomes().add(home);
            plugin.getUserManager().saveUser(user);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Dein Zuhause §e" + homeName + " §7wurde gesetzt.");
            return;
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            String homeName = args[1];
            Home home = user.existsHome(homeName);

            if(home != null) {
                user.getHomes().remove(home);
                plugin.getUserManager().saveUser(user);
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDein Zuhause " + homeName + " wurde gelöscht.");
                return;
            }
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu hast kein Zuhause mit diesem Namen.");
            return;
        }
        plugin.getInventoryManager().openInventory(player, new HomesInventory(plugin, player));
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /home <set,delete> <name>");

    }
}
