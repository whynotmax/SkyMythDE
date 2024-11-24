package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.giveaway.impl.ItemGiveaway;
import de.skymyth.giveaway.impl.RandomPlayerGiveaway;
import de.skymyth.giveaway.impl.TokenGiveaway;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.entity.Player;

import java.util.List;

public class EventCommand extends MythCommand {

    public EventCommand(SkyMythPlugin plugin) {
        super("event", null, List.of("giveaway"), plugin);
    }

    private void sendHelp(Player player) {
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /event add <item> <amount>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /event add <token> <amount>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /event add <randomPlayer>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /event pauseQueue");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /event resumeQueue");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /event clearQueue");
    }

    @Override
    public void run(Player player, String[] args) {
        if (!player.hasPermission("skymyth.event")) {
            if (!plugin.getGiveawayManager().getGiveawayQueue().isEmpty()) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Es läuft derzeit ein Giveaway.");
                return;
            }
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Es läuft derzeit §ckein§7 Giveaway.");
            return;
        }
        if (args.length == 0) {
            sendHelp(player);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "add":
                if (args.length < 2) {
                    sendHelp(player);
                    return;
                }
                switch (args[1].toLowerCase()) {
                    case "item":
                        plugin.getGiveawayManager().addGiveawayToQueue(new ItemGiveaway(plugin, new ItemBuilder(player.getItemInHand()).amount(Integer.parseInt(args[2]))));
                        player.sendMessage(SkyMythPlugin.PREFIX + "§7Das Item Giveaway wurde hinzugefügt.");
                        break;
                    case "token":
                        plugin.getGiveawayManager().addGiveawayToQueue(new TokenGiveaway(plugin, Long.parseLong(args[2])));
                        player.sendMessage(SkyMythPlugin.PREFIX + "§7Das Token Giveaway wurde hinzugefügt.");
                        break;
                    case "randomplayer":
                        plugin.getGiveawayManager().addGiveawayToQueue(new RandomPlayerGiveaway(plugin));
                        player.sendMessage(SkyMythPlugin.PREFIX + "§7Das Random Player Giveaway wurde hinzugefügt.");
                        break;
                    default:
                        sendHelp(player);
                        break;
                }
                break;
            case "pausequeue":
                plugin.getGiveawayManager().pause();
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Giveaway-Queue pausiert.");
                break;
            case "resumequeue":
                plugin.getGiveawayManager().resume();
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Giveaway-Queue entpausiert.");
                break;
            case "clearqueue":
                plugin.getGiveawayManager().clearQueue();
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Giveaway-Queue gelöscht.");
                break;
            default:
                sendHelp(player);
                break;
        }
    }
}
