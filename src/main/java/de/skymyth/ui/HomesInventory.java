package de.skymyth.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.user.model.home.Home;
import de.skymyth.utility.TeleportUtil;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HomesInventory extends AbstractInventory {

    SkyMythPlugin plugin;
    Player player;

    public HomesInventory(SkyMythPlugin plugin, Player player) {
        super("Homes: Menü", 54);
        this.plugin = plugin;
        this.player = player;

        this.defaultInventory();

        AtomicInteger slotCount = new AtomicInteger(10);
        List<Home> homes = plugin.getUserManager().getUser(player.getUniqueId()).getHomes();

        for (Home home : homes) {

            if (slotCount.get() > 16) slotCount.set(19);
            if (slotCount.get() > 25) slotCount.set(28);
            if (slotCount.get() > 34) slotCount.set(37);
            if (slotCount.get() > 43) return;

            setItem(slotCount.getAndIncrement(), new ItemBuilder(Material.WORKBENCH)
                    .setName("§7Home: §e" + home.getName())
                    .lore(
                            "§7Erstellt vor §e" + TimeUtil.beautifyTime((System.currentTimeMillis() - home.getCreated()), TimeUnit.MILLISECONDS, true, true),
                            "",
                            "§7Klicke, um dich dahin zu teleportieren"
                    ), event -> {

                if (home.getLocation() == null) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cHome Position ist ungültig.");
                    return;
                }

                player.closeInventory();
                TeleportUtil.createTeleportation(plugin, player, home.getLocation(), home.getName());
            });
        }

    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
