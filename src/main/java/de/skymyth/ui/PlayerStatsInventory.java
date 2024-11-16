package de.skymyth.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.user.model.User;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.item.ItemBuilder;
import de.skymyth.utility.item.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerStatsInventory extends AbstractInventory {

    SkyMythPlugin plugin;
    UUID statsPlayer;

    public PlayerStatsInventory(SkyMythPlugin plugin, UUID statsPlayer) {
        super("Stats: " + Bukkit.getOfflinePlayer(statsPlayer).getName(), 54);
        this.plugin = plugin;
        this.statsPlayer = statsPlayer;

        defaultInventory();
        User statsUser = plugin.getUserManager().getUser(statsPlayer);
        statsUser.updatePlayTime();

        setItem(22, new ItemBuilder(SkullCreator.itemFromUuid(statsPlayer)).setName("§e" + Bukkit.getOfflinePlayer(statsPlayer).getName()).lore("§7§oDie Statistiken von §e§o" + Bukkit.getOfflinePlayer(statsPlayer).getName()));

        setItem(20, new ItemBuilder(Material.DIAMOND_SWORD).setName("§eKills").lore("§7Kills: §e" + NumberFormat.getInstance(Locale.GERMAN).format(statsUser.getKills())));
        setItem(24, new ItemBuilder(Material.FEATHER).setName("§eTode").lore("§7Tode: §e" + NumberFormat.getInstance(Locale.GERMAN).format(statsUser.getDeaths())));

        setItem(29, new ItemBuilder(Material.DIAMOND).setName("§eGeld").lore("§7Geld: §e" + NumberFormat.getInstance(Locale.GERMAN).format(statsUser.getBalance()).replace(",", ".") + "$"));
        setItem(33, new ItemBuilder(Material.WATCH).setName("§eSpielzeit").lore("§7Spielzeit: §e" + TimeUtil.beautifyTime(statsUser.getPlayTime(), TimeUnit.MILLISECONDS, true, true)));

        setItem(45, new ItemBuilder(Material.BARRIER).setName("§cSchließen").lore("§7§oKlicke, um das Inventar zu schließen."), event -> {
            event.getWhoClicked().closeInventory();
        });
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
