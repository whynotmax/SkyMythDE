package de.skymyth.commands;

import de.skymyth.SkyMythPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class MythCommand extends Command {

    SkyMythPlugin plugin;
    String permission;

    public MythCommand(String name, String permission, SkyMythPlugin plugin) {
        super(name);
        this.permission = permission;
        this.plugin = plugin;
    }

    public abstract void run(Player player, String[] args);

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (player.hasPermission(permission)) {
                run(player, strings);
            } else {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDazu hast du keine Rechte. §8(§c" + permission + "§8)");
            }
        }
        return false;
    }
}
