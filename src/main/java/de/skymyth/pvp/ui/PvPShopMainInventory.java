package de.skymyth.pvp.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.pvp.model.PvPShopItems;
import de.skymyth.user.model.User;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class PvPShopMainInventory extends AbstractInventory {

    private User user;

    public PvPShopMainInventory(User user) {
        super("PvPShop: Menü", 36);

        this.user = user;
        defaultInventory();

        AtomicInteger count = new AtomicInteger(20);
        for (PvPShopItems value : PvPShopItems.values()) {
            setItem(count.getAndIncrement(),
                    new ItemBuilder(value.getItemStack())
                            .setName(value.getDisplayname())
                            .lore(
                                    "",
                                    value.getPrice() > 1 ? "§7Kosten: §e" + NumberFormat.getInstance(Locale.GERMAN).format(value.getPrice()) + " PvP-Shards" : "§cItem global deaktiviert",
                                    "",
                                    "§7Klicke, um dieses Item zu kaufen."
                            ), event -> {
                        Player player = (Player) event.getWhoClicked();

                        if (value.getPrice() < 1) {
                            player.sendMessage(SkyMythPlugin.PREFIX + "§cDieses Item kann im Moment nicht gekauft werden.");
                            return;
                        }

                        if (user.getPvpShards() < value.getPrice()) {
                            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu hast nicht genügend PvP-Shards.");
                            return;
                        }

                        user.removePvPShards(value.getPrice());
                        player.getInventory().addItem(value.getItemStack());
                        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast das Item §e" + value.getDisplayname() + " §7gekauft.");
                    });
            if (count.get() > 25) return;
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
