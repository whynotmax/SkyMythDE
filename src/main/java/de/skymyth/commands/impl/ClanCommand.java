package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;
import java.util.ArrayList;

public class ClanCommand extends MythCommand {

    public ClanCommand(SkyMythPlugin plugin) {
        super("clan", "", new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        // /Clan create <name>

        if(args.length == 2 && args[0].equalsIgnoreCase("create")) {

            String clanName = args[1];

            if (!clanName.matches("[A-Za-z0-9]+")) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cBitte verwende keine Sonderzeichen im Clannamen.");
                return;
            }

            if(clanName.length() < 2) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Clanname ist zu kurz.");
                return;
            }

            if(clanName.length() > 5) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Clanname ist zu lange.");
                return;
            }

            if(plugin.getClanManager().existsClan(clanName)) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Clanname ist bereits vergeben.");
                return;
            }

            plugin.getClanManager().createClan(player.getUniqueId(), clanName);



            return;
        }
    }
}
