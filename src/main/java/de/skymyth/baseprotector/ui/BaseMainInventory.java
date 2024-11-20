package de.skymyth.baseprotector.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.baseprotector.model.BaseProtector;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class BaseMainInventory extends AbstractInventory {

    SkyMythPlugin plugin;

    public BaseMainInventory(Player player, SkyMythPlugin plugin) {
        super("Basisschutz: Menü", 27);
        this.plugin = plugin;

        this.defaultInventory();

        // 10-16
        setItem(13, new ItemBuilder(Material.BARRIER)
                .setName("§cBaseschutz zerstören")
                .lore(
                        "",
                        "§7Zerstöre deinen Basisschutz und erhalte ihn neu.",
                        "§7Alles im Umkreis befindene ist daraufhin ungeschützt.",
                        "",
                        "§cKlicke, um deinen Basisschutz zu zerstören."
                ), event -> {

            player.closeInventory();
            BaseProtector baseProtector = plugin.getBaseProtectorManager().getBaseProtector(player.getUniqueId());

            if (baseProtector != null) {
                Location location = baseProtector.getBaseProtectorLocation();
                Block block = location.getBlock();

                plugin.getBaseProtectorManager().deleteBaseProtection(player.getUniqueId());
                block.setType(Material.AIR);
                location.getWorld().dropItemNaturally(location,
                        plugin.getBaseProtectorManager().getBaseProtectorItem());
                player.playSound(location, Sound.CHICKEN_EGG_POP, 1, 1);
            }
        });

        setItem(11, new ItemBuilder(Material.NAME_TAG)
                .setName("§aSpieler einladen")
                .lore(
                        "",
                        "§7Lade bis zu 10 Spieler ein und erbaut zusammen",
                        "§7euer eigenes Dorf, eure Farms oder Lager.",
                        "",
                        "§aKlicke, um Spieler zum mitbauen einzuladen"
                ), event -> {
            player.closeInventory();
            BaseProtector baseProtector = plugin.getBaseProtectorManager().getBaseProtector(player.getUniqueId());

            if (baseProtector != null) {
                plugin.getInventoryManager().openInventory(player, new BasePlayersInventory(plugin, player, baseProtector));
            }
        });

        setItem(15, new ItemBuilder(Material.STRING)
                .setName("§6Radius erweitern")
                .lore(
                        "",
                        "§7Erweitere den Radius deines Basisschutzes",
                        "§7um mehr Platz zum bauen zu haben.",
                        "",
                        "§aKlicke, um deinen Basisradius zu erweitern"
                ), event -> {

        });
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
