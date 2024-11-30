package de.skymyth.clan.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.clan.model.Clan;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ClanBankInventory extends AbstractInventory {

    SkyMythPlugin plugin;
    Clan clan;
    boolean isOwner;

    public ClanBankInventory(SkyMythPlugin plugin, Clan clan, boolean isOwner) {
        super("Clanbank: " + clan.getName(), 27);
        this.plugin = plugin;
        this.clan = clan;
        this.isOwner = isOwner;

        defaultInventory();

        setItems();

    }

    public void setItems() {
        ItemBuilder infoItem = new ItemBuilder(Material.SIGN).setName("§8» §eClanbank Info").lore("§7Hier kann dein Clan Tokens sparen.", "§r", "§7Tokens: §e" + NumberFormat.getInstance(Locale.GERMAN).format(clan.getBank().getBalance()).replaceAll(",", "."));
        if (clan.getBank().getLastDeposit() == 0) {
            infoItem.addToLore("§7Letzte Einzahlung: §cnie");
        } else {
            infoItem.addToLore("§7Letzte Einzahlung: §e" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(clan.getBank().getLastDeposit()));
        }
        if (isOwner) {
            if (clan.getBank().getLastWithdraw() == 0) {
                infoItem.addToLore("§7Letzte Auszahlung: §cnie");;
            } else {
                infoItem.addToLore("§7Letzte Auszahlung: §e" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(clan.getBank().getLastWithdraw()));
            }
        }
        infoItem.addToLore("§r", "§7Rechtsklick, um Geld einzuzahlen.");
        if (isOwner) {
            infoItem.addToLore("§7Linksklick, um Geld auszuzahlen.");
        }

        setItem(13, infoItem);

        setItem(11, new ItemBuilder(Material.EMERALD).setName("§8» §aEinzahlen").lore("§7Klicke, um Geld einzuzahlen."), (event -> {
            event.setCancelled(true);
            if (event.isRightClick()) {
                // deposit conversation
            }
        }));

        if (isOwner) {
            setItem(15, new ItemBuilder(Material.REDSTONE).setName("§8» §cAuszahlen").lore("§7Klicke, um Geld auszuzahlen."), (event -> {
                event.setCancelled(true);
                if (event.isLeftClick()) {
                    // withdraw conversation
                }
            }));
        } else {
            setItem(15, new ItemBuilder(Material.BARRIER).setName("§8» §cAuszahlen").lore("§7Nur der Clanbesitzer kann Geld auszahlen"));
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
