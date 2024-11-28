package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.bounties.BountyManager;
import de.skymyth.bounties.model.Bounty;
import de.skymyth.commands.MythCommand;
import de.skymyth.user.model.User;
import de.skymyth.utility.UUIDFetcher;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.LongStream;

/*
public class BountyCommand extends MythCommand {

    public BountyCommand(SkyMythPlugin plugin) {
        super("bounty", null, List.of("kopfgeld", "bounties", "kopfgelder"), plugin);
    }

    private void sendHelp(Player player) {
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /bounty");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /bounty list");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /bounty <Spieler>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /bounty <Spieler> <Betrag>");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            plugin.getInventoryManager().openInventory(player, new BountyInventory(this.plugin, player.getUniqueId()));
            return;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                plugin.getInventoryManager().openInventory(player, new BountyInventory(this.plugin, player.getUniqueId()));
                return;
            }
            UUID target = UUIDFetcher.getUUID(args[0]);
            if (target == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Dieser Spieler existiert nicht.");
                return;
            }
            long reward = 0;
            for (Bounty bounty : plugin.getBountyManager().getBounties(target)) {
                reward += bounty.getReward();
            }
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Das Kopfgeld von §e" + args[0] + " §7beträgt §e" + NumberFormat.getInstance(Locale.GERMAN).format(reward) + " ⛃");
            return;
        }
        if (args.length == 2) {
            UUID target = UUIDFetcher.getUUID(args[0]);
            if (target == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Dieser Spieler existiert nicht.");
                return;
            }
            try {
                long reward = Long.parseLong(args[1]);
                if (reward < 500) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Betrag muss mindestens 500 ⛃ betragen.");
                    return;
                }
                if (reward > 1000000) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Betrag darf maximal 1.000.000 ⛃ betragen.");
                    return;
                }
                User user = plugin.getUserManager().getUser(player.getUniqueId());
                if (user.getBalance() < reward) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu hast nicht genügend Geld.");
                    return;
                }
                user.removeBalance(reward);
                plugin.getUserManager().saveUser(user);
                Bounty bounty = plugin.getBountyManager().getBounty(target);
                if (bounty == null) {
                    bounty = new Bounty();
                    bounty.setTarget(target);
                    bounty.setHunters(new HashMap<>() {{
                        put(player.getUniqueId(), reward);
                    }});
                } else {
                    bounty.getHunters().put(player.getUniqueId(), reward);
                }
                plugin.getBountyManager().saveBounty(bounty);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast ein Kopfgeld auf §e" + args[0] + " §7ausgesetzt.");
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Kopfgeld: §e" + NumberFormat.getInstance(Locale.GERMAN).format(reward) + " ⛃");
                Player targetPlayer = plugin.getServer().getPlayer(target);
                if (targetPlayer != null) {
                    targetPlayer.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast ein Kopfgeld von §e" + player.getName() + " §7erhalten.");
                    targetPlayer.sendMessage(SkyMythPlugin.PREFIX + "§7Kopfgeld: §e" + NumberFormat.getInstance(Locale.GERMAN).format(reward) + " ⛃");
                    targetPlayer.sendMessage(SkyMythPlugin.PREFIX + "§7Insgesamt: §e" + NumberFormat.getInstance(Locale.GERMAN).format(LongStream.of(plugin.getBountyManager().sortedBounties().get(target)).sum()) + " ⛃");
                }
                return;
            } catch (NumberFormatException e) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /bounty <Spieler> <Betrag>");
                return;
            }
        }
        sendHelp(player);
    }
}

 */
