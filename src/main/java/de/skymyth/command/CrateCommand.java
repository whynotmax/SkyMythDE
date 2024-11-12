package de.skymyth.command;

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
                    player.sendMessage(plugin.getPrefix() + "§cDiese Crate existiert bereits.");
                    return false;
                }


                Crate crate = plugin.getCrateManager().createCrate(name);
                player.sendMessage(plugin.getPrefix() + "§7Die Crate §a" + name + " §7wurde §aerstellt.");

                plugin.getCrateManager().setDisplayItem(crate, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZkMTA4MzgzZGZhNWIwMmU4NjYzNTYwOTU0MTUyMGU0ZTE1ODk1MmQ2OGMxYzhmOGYyMDBlYzdlODg2NDJkIn19fQ==");
                return false;
            }

            if(args.length == 2 && args[0].equalsIgnoreCase("give")) {

                String name = args[1];

                if (!plugin.getCrateManager().existsCrate(name)) {
                    player.sendMessage(plugin.getPrefix() + "§cDiese Crate existiert nicht.");
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
