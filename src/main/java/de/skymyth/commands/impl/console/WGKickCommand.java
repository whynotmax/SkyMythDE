package de.skymyth.commands.impl.console;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.TimeUtil;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WGKickCommand extends Command {

    public WGKickCommand() {
        super("wgkick", "", "", List.of());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender instanceof org.bukkit.command.ConsoleCommandSender) {
            String playerName = strings[0];
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                commandSender.sendMessage("Der Spieler ist nicht online.");
                return true;
            }
            PlayerConnection playerConnection = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer) player).getHandle().playerConnection;
            playerConnection.disconnect("§r\n" +
                    "§cDu wurdest von SkyMyth.DE gekickt.\n" +
                    "§r\n" +
                    "§7Grund: §eDu sendest misstrauische Pakete.\n" +
                    "§r");
            return true;
        }
        commandSender.sendMessage(SkyMythPlugin.PREFIX + "§cDazu hast du keine Rechte.");
        return false;
    }
}
