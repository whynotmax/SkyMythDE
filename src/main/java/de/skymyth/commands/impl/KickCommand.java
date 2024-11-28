package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.punish.model.Punish;
import de.skymyth.utility.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class KickCommand extends MythCommand {

    public KickCommand(SkyMythPlugin plugin) {
        super("kick", "myth.kick", new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {


        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            target.kickPlayer(this.getKickScreen("Kein Grund angegeben"));
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + target.getName() + " §7gekickt.");
            return;
        }
        
        if(args.length > 1) {
            Player target = Bukkit.getPlayer(args[0]);
            StringBuilder stringBuilder = new StringBuilder();

            for (String arg : args) {
                stringBuilder.append(arg).append(" ");
            }

            target.kickPlayer(this.getKickScreen(stringBuilder.toString()));
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + target.getName() + " §7gekickt.");
            return;
        }
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /kick spieler <grund>");
    }

    private String getKickScreen(String reason) {
        return "§r\n" +
                "§cDu wurdest von SkyMyth.DE gekickt.\n" +
                "§r\n" +
                "§7Grund: §e" + reason + "\n" +
                "§r";
    }
}
