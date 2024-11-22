package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.baseprotector.model.BaseProtector;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.UUIDFetcher;
import de.skymyth.utility.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class BaseCommand extends MythCommand {

    public BaseCommand(SkyMythPlugin plugin) {
        super("base", null, List.of("baseprotector"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("accept")) {

            BaseProtector baseProtector = Util.BASEINVITE.asMap().get(player);

            if (!Util.BASEINVITE.asMap().containsKey(player)) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu wurdest in keinen Basisschutz eingeladen.");
                return;
            }

            if(baseProtector == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Basisschutz existiert nicht mehr.");
                return;
            }

            baseProtector.addTrustedPlayer(player.getUniqueId());
            plugin.getBaseProtectorManager().saveBaseProtection(baseProtector);
            player.sendMessage(SkyMythPlugin.PREFIX + "§aDu bist nun ein Mitbauer von diesen Basisschutz.");

            for (UUID trustedPlayer : baseProtector.getTrustedPlayers()) {
                Player trusted = Bukkit.getPlayer(trustedPlayer);

                if (trusted == null) continue;
                trusted.sendMessage(SkyMythPlugin.PREFIX + "§e" + player.getName() + " §7ist nun ein Mitbauer von §e" +
                        UUIDFetcher.getName(baseProtector.getBaseOwner()) + "'s Basisschutz.");
                Bukkit.getPlayer(baseProtector.getBaseOwner()).sendMessage(
                        SkyMythPlugin.PREFIX + "§e" + player.getName() + " §7ist nun ein Mitbauer von §e" +
                                UUIDFetcher.getName(baseProtector.getBaseOwner()) + "'s Basisschutz."
                );
                Util.BASEINVITE.invalidate(player);

            }
            return;
        }

    }
}
