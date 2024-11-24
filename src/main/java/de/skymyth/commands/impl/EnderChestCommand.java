package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class EnderChestCommand extends MythCommand {

    public EnderChestCommand(SkyMythPlugin plugin) {
        super("enderchest", null, List.of("lager", "ec"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        if(args.length == 1 && player.isOp()) {
            Player target = Bukkit.getPlayer(args[0]);

            if(target == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler ist nicht online.");
                return;
            }

            player.openInventory(target.getEnderChest());
            return;
        }
        player.openInventory(player.getEnderChest());
    }
}
