package de.skymyth.commands;

import de.skymyth.SkyMythPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class MythCommand extends Command {

    SkyMythPlugin plugin;
    String permission;
    List<String> aliases;

    public MythCommand(String name, String permission, List<String> aliases, SkyMythPlugin plugin) {
        super(name, "", "", aliases);
        this.permission = permission;
        this.aliases = aliases;
        this.plugin = plugin;
    }

    public abstract void run(Player player, String[] args);

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (permission == null || player.hasPermission(permission)) {
                run(player, strings);
            } else {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDazu hast du keine Rechte. §8(§c" + permission + "§8)");
            }
        }
        return false;
    }
}
