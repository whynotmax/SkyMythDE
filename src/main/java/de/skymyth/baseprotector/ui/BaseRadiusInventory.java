package de.skymyth.baseprotector.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.baseprotector.model.BaseProtector;
import de.skymyth.baseprotector.model.radius.BaseProtectorRadius;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.user.model.User;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseRadiusInventory extends AbstractInventory {

    SkyMythPlugin plugin;
    Player player;

    public BaseRadiusInventory(SkyMythPlugin plugin, Player player) {
        super("Basisschutz: Radius", 36);

        this.plugin = plugin;
        this.player = player;

        this.defaultInventory();
        setItem(27, new ItemBuilder(Material.BARRIER).setName("§cZurück"), inventoryClickEvent -> plugin.getInventoryManager().openInventory(player, new BaseMainInventory(player, plugin)));

        BaseProtector baseProtector = plugin.getBaseProtectorManager().getBaseProtector(player.getUniqueId());
        User user = plugin.getUserManager().getUser(player.getUniqueId());

        AtomicInteger slotCounter = new AtomicInteger(10);
        for (BaseProtectorRadius baseProtectorRadius : BaseProtectorRadius.values()) {

            if (slotCounter.get() == 17) {
                slotCounter.set(19);
            }

            if (slotCounter.get() > 25) return;

            setItem(slotCounter.getAndIncrement(), new ItemBuilder(Material.POWERED_RAIL)
                    .setName("§7Basisradius: §e" + baseProtectorRadius.getRadius() + "§8x§e" + baseProtectorRadius.getRadius())
                    .lore(
                            "",
                            "§7Diese Stufe setzt deinen Basisschutz Radius auf §e" + baseProtectorRadius.getRadius() + "§8x§e" + baseProtectorRadius.getRadius(),
                            "",
                            "§7Kosten: §e" + (baseProtectorRadius.getPrice() < 1 ? "Kostenfrei" : NumberFormat.getInstance(Locale.GERMAN).format(baseProtectorRadius.getPrice())) + " §e⛃",

                            "",
                            (baseProtector.getBaseProtectorRadius() == baseProtectorRadius ? "§aDu besitzt im Moment diesen Radius." : "§cKlicke, um diese Stufe zu kaufen.")
                    ), event -> {

                if (baseProtectorRadius.getRadius() <= baseProtector.getBaseProtectorRadius().getRadius()) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst keinen niedrigeren Basisschutz Radius kaufen.");
                    return;
                }

                if (user.getBalance() < baseProtectorRadius.getPrice()) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu hast nicht genügend Tokens.");
                    return;
                }

                user.removeBalance(baseProtectorRadius.getPrice());
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast den §eBasisschutz Radius §7erworben.");
                baseProtector.setBaseProtectorRadius(baseProtectorRadius);

                player.closeInventory();
                plugin.getInventoryManager().openInventory(player, new BaseRadiusInventory(plugin, player));
                return;


            });
        }

    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
