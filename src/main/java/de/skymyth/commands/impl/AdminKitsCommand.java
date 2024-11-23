package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.kit.model.Kit;
import de.skymyth.kit.model.type.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AdminKitsCommand extends MythCommand {

    public AdminKitsCommand(SkyMythPlugin plugin) {
        super("adminkits", "myth.owner", List.of("adminkit", "akits", "akit"), plugin);
    }

    public void sendHelp(Player player) {
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende:");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/akits create <name> <display_item>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/akits delete <name>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/akits additem <kit>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/akits clear <kit>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/akits type <kit> <rank_specific/one_time/buyable/system_only>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/akits permission <kit> <permission>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/akits cooldown <kit> <minutes>");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            this.sendHelp(player);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "create":
                if (args.length < 3) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /akits create <name> <display_item>");
                    return;
                }
                plugin.getKitManager().createKit(args[1], Material.valueOf(args[2]), KitType.RANK_SPECIFIC, new ArrayList<>());
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Kit §e" + args[1] + " §7erstellt.");
                break;
            case "delete":
                if (args.length < 2) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /akits delete <name>");
                    return;
                }
                plugin.getKitManager().deleteKit(args[1]);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Kit §e" + args[1] + " §7gelöscht.");
                break;
            case "additem":
                if (args.length < 2) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /akits additem <kit>");
                    return;
                }
                Kit kit = plugin.getKitManager().getKitByName(args[1]);
                if (kit == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDas Kit §e" + args[1] + " §cexistiert nicht.");
                    return;
                }
                List<ItemStack> items = new ArrayList<>(kit.getItems());
                items.add(player.getItemInHand());
                kit.setItems(items);
                plugin.getKitManager().saveKit(kit);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Item hinzugefügt.");
                break;
            case "clear":
                if (args.length < 2) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /akits clear <kit>");
                    return;
                }
                Kit kitOne = plugin.getKitManager().getKitByName(args[1]);
                if (kitOne == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDas Kit §e" + args[1] + " §cexistiert nicht.");
                    return;
                }
                kitOne.setItems(new ArrayList<>());
                plugin.getKitManager().saveKit(kitOne);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Items gelöscht.");
                break;
            case "type":
                if (args.length < 3) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /akits isrank <kit> <true/false>");
                    return;
                }
                Kit kitTwo = plugin.getKitManager().getKitByName(args[1]);
                if (kitTwo == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDas Kit §e" + args[1] + " §cexistiert nicht.");
                    return;
                }

                if(args[2].equalsIgnoreCase("system_only")) {
                    kitTwo.setSystemOnly(!kitTwo.isSystemOnly());
                    plugin.getKitManager().saveKit(kitTwo);
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Kit ist nun §eSYSTEM_ONLY");
                    return;
                }

                kitTwo.setType(KitType.valueOf(args[2].toUpperCase()));
                plugin.getKitManager().saveKit(kitTwo);
                switch (kitTwo.getType()) {
                    case RANK_SPECIFIC:
                        player.sendMessage(SkyMythPlugin.PREFIX + "§7Kit ist nun §eRANK_SPECIFIC§7.");
                        break;
                    case ONE_TIME:
                        player.sendMessage(SkyMythPlugin.PREFIX + "§7Kit ist nun §eONE_TIME§7.");
                        break;
                    case BUYABLE:
                        player.sendMessage(SkyMythPlugin.PREFIX + "§7Kit ist nun §eBUYABLE§7.");
                        break;
                    default:
                        break;
                }
                break;
            case "permission":
                if (args.length < 3) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /akits permission <kit> <permission>");
                    return;
                }
                Kit kitThree = plugin.getKitManager().getKitByName(args[1]);
                if (kitThree == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDas Kit §e" + args[1] + " §cexistiert nicht.");
                    return;
                }
                kitThree.setPermission(args[2]);
                plugin.getKitManager().saveKit(kitThree);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Kit hat nun die Permission §e" + args[2] + "§7.");
                break;
            case "cooldown":
                if (args.length < 3) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /akits cooldown <kit> <minutes>");
                    return;
                }
                Kit kitFour = plugin.getKitManager().getKitByName(args[1]);
                if (kitFour == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDas Kit §e" + args[1] + " §cexistiert nicht.");
                    return;
                }
                kitFour.setCooldown(Duration.ofMinutes(Long.parseLong(args[2])));
                plugin.getKitManager().saveKit(kitFour);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Kit hat nun einen Cooldown von §e" + args[2] + " §7Minuten.");
                break;
            default:
                this.sendHelp(player);
                break;
        }
        return;
    }
}
