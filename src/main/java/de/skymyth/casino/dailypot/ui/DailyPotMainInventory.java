package de.skymyth.casino.dailypot.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.casino.dailypot.model.DailyPot;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.user.model.User;
import de.skymyth.utility.item.ItemBuilder;
import de.skymyth.utility.item.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import java.text.NumberFormat;
import java.util.Locale;

public class DailyPotMainInventory extends AbstractInventory {

    SkyMythPlugin plugin;

    DailyPot dailyPot;

    public DailyPotMainInventory(SkyMythPlugin plugin) {
        super("DailyPot: Menü", InventoryType.HOPPER);
        this.plugin = plugin;
        this.dailyPot = plugin.getCasinoManager().getDailyPotManager().getDailyPot();

        setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setDataId(15).setName("§r"));
        setItem(2, new ItemBuilder(Material.STAINED_GLASS_PANE).setDataId(15).setName("§r"));
        setItem(4, new ItemBuilder(Material.STAINED_GLASS_PANE).setDataId(15).setName("§r"));

        setItem(1, new ItemBuilder(Material.STAINED_GLASS_PANE).setDataId(5).setName("§aTeilnehmen").lore(
                "§r",
                "§7§oEs nehmen bereits §e§o" + dailyPot.getParticipants().size() + " §7§oSpieler teil.",
                "§7§oDeine Gewinnchance beträgt §e§o" + dailyPot.calculateWinChance() + "%.",
                "§7§oDer aktuelle Pot beträgt §e§o" + NumberFormat.getInstance(Locale.GERMAN).format(dailyPot.getPot()).replace(",", ".") + "$§7§o.",
                "§r",
                "§7Klicke, um am DailyPot teilzunehmen."
        ), event -> {
            User user = plugin.getUserManager().getUser(event.getWhoClicked().getUniqueId());
            if (user.getBalance() < 100) {
                event.getWhoClicked().sendMessage(SkyMythPlugin.PREFIX + "§cDazu ist dein Kontostand zu niedrig.");
                return;
            }
            user.removeBalance(100);
            plugin.getUserManager().saveUser(user);
            plugin.getCasinoManager().getDailyPotManager().joinDailyPot((Player) event.getWhoClicked());
            event.getWhoClicked().closeInventory();
        });

        if (dailyPot.getLastWinner() == null) {
            setItem(3, new ItemBuilder(Material.STAINED_GLASS_PANE).setDataId(14).setName("§cLetzter Gewinner").lore(
                    "§r",
                    "§7§oDer letzte Gewinner ist §c§oUnbekannt",
                    "§7§oEr hat §e§o0$ §7§ogewonnen.",
                    "§r"
            ));
            return;
        }

        setItem(3, new ItemBuilder(plugin.getSkullLoader().getSkull(dailyPot.getLastWinner())).setName("§6Letzter Gewinner").lore(
                "§r",
                "§7§oDer letzte Gewinner war §e§o" + (dailyPot.getLastWinner() == null ? "§c§oUnbekannt" : Bukkit.getOfflinePlayer(dailyPot.getLastWinner()).getName()),
                "§7Er hat §e§o" + NumberFormat.getInstance(Locale.GERMAN).format(dailyPot.getLastWinnerPot()).replace(",", ".") + "$ §7§ogewonnen.",
                "§r"
        ));

    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
