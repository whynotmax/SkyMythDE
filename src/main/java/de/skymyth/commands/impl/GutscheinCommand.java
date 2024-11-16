package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.badge.model.Badge;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.item.ItemBuilder;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class GutscheinCommand extends MythCommand {

    public GutscheinCommand(SkyMythPlugin plugin) {
        super("gutschein", "myth.admin", List.of("gutscheine"), plugin);
    }

    private void sendHelp(Player player) {
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende:");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/gutschein balance <betrag>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/gutschein badge <name>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/gutschein kit <name>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7/gutschein rank <name>");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            this.sendHelp(player);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "balance":
                if (args.length < 2) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /gutschein balance <betrag>");
                    return;
                }
                long balance;
                try {
                    balance = Long.parseLong(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cUngültiger Betrag.");
                    return;
                }
                player.getInventory().addItem(getBalanceVoucher(balance));
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast einen Gutschein über §e" + NumberFormat.getInstance(Locale.GERMAN).format(balance).replace(",", ".") + " §7Coins erhalten.");
                break;
            case "badge":
                if (args.length < 2) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /gutschein badge <name>");
                    return;
                }
                player.getInventory().addItem(getBadgeVoucher(args[1]));
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast einen Gutschein für das Badge §e" + args[1] + " §7erhalten.");
                break;
            case "kit":
                if (args.length < 2) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /gutschein kit <name>");
                    return;
                }
                player.getInventory().addItem(getKitVoucher(args[1]));
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast einen Gutschein für das Kit §e" + args[1] + " §7erhalten.");
                break;
            case "rank":
                throw new UnsupportedOperationException("Not implemented yet.");
            default:
                this.sendHelp(player);
                break;
        }
    }

    private ItemStack getBalanceVoucher(long balance) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.DOUBLE_PLANT);
        itemBuilder.setName("§7Gutschein§8: §e" + NumberFormat.getInstance(Locale.GERMAN).format(balance).replace(",", ".") + " §7Coins");
        itemBuilder.lore(
                "§8§m------------------------------------------------------",
                "§7Mit diesem Gutschein kannst du",
                "§7dir §e" + NumberFormat.getInstance(Locale.GERMAN).format(balance).replace(",", ".") + " §7Coins",
                "§7auf deinem Konto gutschreiben lassen.",
                "§8§m------------------------------------------------------"
        );
        addNBTTag(itemBuilder, "balance", String.valueOf(balance));
        addNBTTag(itemBuilder, "voucher", "balance");
        return itemBuilder;
    }

    private ItemStack getBadgeVoucher(String badgeName) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.PAPER);
        Badge badge = plugin.getBadgeManager().getBadge(badgeName);
        if (badge == null) {
            return null;
        }
        itemBuilder.setName("§7Gutschein§8: §e" + badge.getCharacter() + "§8 (§7Badge§8)");
        itemBuilder.lore(
                "§8§m------------------------------------------------------",
                "§7Mit diesem Gutschein kannst du",
                "§7dir das Badge §8'§e" + badge.getCharacter() + "§8' §7freischalten.",
                "§8§m------------------------------------------------------"
        );
        addNBTTag(itemBuilder, "badge", badge.getName());
        addNBTTag(itemBuilder, "voucher", "badge");
        return itemBuilder;
    }

    private ItemStack getKitVoucher(String kitName) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.CHEST);
        itemBuilder.setName("§7Gutschein§8: §e" + kitName + " §8(§eKit§8)");
        itemBuilder.lore(
                "§8§m------------------------------------------------------",
                "§7Mit diesem Gutschein kannst du",
                "§7dir das Kit §8'§e" + kitName + "§8' §7einmal einlösen.",
                "§8§m------------------------------------------------------"
        );
        addNBTTag(itemBuilder, "kit", kitName);
        addNBTTag(itemBuilder, "voucher", "kit");
        return itemBuilder;
    }

    private void addNBTTag(ItemBuilder itemBuilder, String key, String value) {
        NBTTagCompound nbtTagCompound = itemBuilder.getNBTTagCompound();
        nbtTagCompound.setString(key, value);
        itemBuilder.setNBTTagCompound(nbtTagCompound);
    }
}
