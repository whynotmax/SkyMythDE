package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.crate.model.Crate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public record CrateCommand(SkyMythPlugin plugin) implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {


        if(sender instanceof Player player) {
            // /Crate create <name>
            if (args.length == 2 && args[0].equalsIgnoreCase("create")) {

                String name = args[1];

                if (plugin.getCrateManager().existsCrate(name)) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDiese Crate existiert bereits.");
                    return false;
                }


                Crate crate = plugin.getCrateManager().createCrate(name);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Die Crate §a" + name + " §7wurde §aerstellt.");
                return false;
            }

            if(args.length == 2 && args[0].equalsIgnoreCase("give")) {

                String name = args[1];

                if (!plugin.getCrateManager().existsCrate(name)) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDiese Crate existiert nicht.");
                    return false;
                }

                Crate crate = plugin.getCrateManager().getCrate(name);

                player.getInventory().addItem(crate.getDisplayItem());

                return false;
            }
        }
        return false;
    }
}
