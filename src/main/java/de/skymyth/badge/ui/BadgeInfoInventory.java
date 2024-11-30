package de.skymyth.badge.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.badge.model.Badge;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import java.text.SimpleDateFormat;

public class BadgeInfoInventory extends AbstractInventory {

    SkyMythPlugin plugin;

    public BadgeInfoInventory(SkyMythPlugin plugin, Player opener, Badge badge) {
        super("Badge Info", InventoryType.DISPENSER);
        this.plugin = plugin;

        for (int i = 0; i < 9; i++) {
            setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDataId(15).setName(" "));
        }

        ItemBuilder badgeItem = new ItemBuilder(Material.PAPER);
        badgeItem.setName("§8[§e" + badge.getColor() + badge.getCharacter() + "§8] §7Badge");
        badgeItem.lore(
                "§8Badge-ID: " + badge.getName(),
                "§r",
                "§7§o" + badge.getDescription(),
                "§r",
                "§7Besitzer: §e" + badge.getOwners().size(),
                "§7Erstellt um: §e" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(badge.getCreationDate()),
                "§7Erstellt von: §e" + plugin.getServer().getOfflinePlayer(badge.getCreator()).getName(),
                "§r",
                (badge.getOwners().contains(opener.getUniqueId()) ? "§aDu besitzt dieses Badge." : "§cDu besitzt dieses Badge nicht.")
        );
        setItem(4, badgeItem);
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
