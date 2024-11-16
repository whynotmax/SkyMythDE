package de.skymyth.perks.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.perks.model.Perks;
import de.skymyth.user.model.User;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.item.ItemBuilder;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PerkInventory extends AbstractInventory {

    SkyMythPlugin plugin;
    User user;

    public PerkInventory(SkyMythPlugin plugin, User user) {
        super("Perks: Menü", 36);
        this.plugin = plugin;
        this.user = user;

        defaultInventory();

        int i = 10;
        for (Perks perk : Perks.VALUES) {
            ItemBuilder itemBuilder = new ItemBuilder(perk.getDisplayItem());
            itemBuilder.setName("§e" + perk.getName());
            List<String> lore = new ArrayList<>();
            lore.add("§7§o" + perk.getDescription());
            lore.add("§r");
            lore.add("§7Status: " + (user.hasPerk(perk) ? "§aaktiviert" : "§cdeaktiviert"));
            if (perk.getPrice() >= 1) {
                lore.add("§7Kosten: §e" + NumberFormat.getInstance(Locale.GERMAN).format(perk.getPrice()).replace(",", ".") + "$");
            } else {
                lore.add("§7Kosten: §cPerk global deaktiviert");
            }
            lore.add("§r");
            if (!user.hasPerk(perk)) {
                lore.add("§7Klicke, um den Perk für §e" + TimeUtil.beautifyTime(perk.getDurationPerPrice().toMillis(), TimeUnit.MILLISECONDS, true, true) + " §7zu kaufen.");
            }
            itemBuilder.lore(lore);
            setItem(i, itemBuilder, event -> {
                if (user.hasPerk(perk)) {
                    return;
                }
                Player player = (Player) event.getWhoClicked();
                long price = perk.getPrice();
                if (price < 1) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDieses Perk kann im Moment nicht gekauft werden.");
                    return;
                }
                if (user.getBalance() < price) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu hast nicht genügend Geld.");
                    return;
                }
                user.addPerk(perk, perk.getDurationPerPrice().toMillis());
                user.removeBalance(price);
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast den Perk §e" + perk.getName() + " §7gekauft.");
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Der Perk ist nun für §e" + TimeUtil.beautifyTime(perk.getDurationPerPrice().toMillis(), TimeUnit.MILLISECONDS, true, true) + " §7aktiviert.");
                player.closeInventory();
            });
            i++;
            if (i == 17) {
                i = 19;
            }
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
