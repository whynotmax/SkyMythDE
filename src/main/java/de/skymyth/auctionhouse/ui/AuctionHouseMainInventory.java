package de.skymyth.auctionhouse.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.auctionhouse.filter.AuctionHouseItemFilter;
import de.skymyth.auctionhouse.model.AuctionHouseItem;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.user.model.User;
import de.skymyth.utility.RandomUtil;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.TitleUtil;
import de.skymyth.utility.UUIDFetcher;
import de.skymyth.utility.item.ItemBuilder;
import de.skymyth.utility.pagination.Pagination;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AuctionHouseMainInventory extends AbstractInventory {

    SkyMythPlugin plugin;
    Pagination<AuctionHouseItem> pagination;
    int page;

    public AuctionHouseMainInventory(SkyMythPlugin plugin) {
        super("Auktionen: Alle", 54);
        this.plugin = plugin;

        defaultInventory();

        this.page = 0;
        this.pagination = new Pagination<>(0, 28);

        plugin.getAuctionHouseManager().getAuctionHouseItems().forEach(this.pagination::addItem);

        update(0);
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }

    private void update(int newPage) {
        this.page = newPage;

        defaultInventory();

        setItem(47, new ItemBuilder(Material.HOPPER).setName("§aItems filtern").lore(
                "§7Filtere die Items nach:",
                "§8- §7Preis (günstigste zuerst)",
                "§8- §7Preis (teuerste zuerst)",
                "§8- §7Ablaufdatum (früheste zuerst)",
                "§8- §7Ablaufdatum (späteste zuerst)"
        ), event -> {
            Player player = (Player) event.getWhoClicked();
            player.closeInventory();
            Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getInventoryManager().openInventory(player, new AuctionHouseFilterInventory(plugin, AuctionHouseItemFilter.PRICE_LOW_TO_HIGH)), 2L);
        });

        setItem(48, new ItemBuilder(Material.ANVIL).setName("§aItems suchen").lore(
                "§7Suche Items nach einem bestimmten Namen"
        ), event -> {
            Player player = (Player) event.getWhoClicked();
            player.closeInventory();
            AnvilGUI.Builder builder = new AnvilGUI.Builder()
                    .plugin(plugin)
                    .disableGeyserCompat()
                    .text("Suche nach einem Item")
                    .title("Auktionen: Suche")
                    .itemLeft(new ItemBuilder(Material.PAPER).lore("§7Gib den Namen des Items ein, welches du suchen möchtest."))
                    .onClose(player1 -> Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getInventoryManager().openInventory(player, new AuctionHouseMainInventory(plugin)), 2L))
                    .onClick((integer, stateSnapshot) -> {
                        String itemName = stateSnapshot.getOutputItem().getItemMeta().getDisplayName();
                        if (itemName == null || itemName.isEmpty() || itemName.equalsIgnoreCase("Suche nach einem Item")) {
                            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst einen Namen eingeben.");
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        }
                        player.closeInventory();
                        return Collections.singletonList(AnvilGUI.ResponseAction.run(() -> {
                            TitleUtil.sendTitle(player, 0, 60, 0, "§aSuche nach '" + itemName + "'", "§7Bitte warte einen Moment...");
                            Bukkit.getScheduler().runTaskLater(plugin, player::closeInventory, 2L);
                            Bukkit.getScheduler().runTaskLater(plugin, player::closeInventory, 4L);
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                plugin.getInventoryManager().openInventory(player, new AuctionHouseNameFilterInventory(plugin, itemName));
                                TitleUtil.sendTitle(player, 0, 5, 0, "§r", "§r");
                            }, 35L);
                            player.closeInventory();
                        }));
                    });
            Bukkit.getScheduler().runTaskLater(plugin, () -> builder.open(player), 2L);
        });

        setItem(50, new ItemBuilder(Material.HOPPER_MINECART).setName("§cDeine abgelauften Auktionen").lore(
                "§7Hier findest du alle deine abgelaufenen Auktionen"
        ), event -> {
            Player player = (Player) event.getWhoClicked();
            player.closeInventory();
            Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getInventoryManager().openInventory(player, new AuctionHouseExpiredInventory(plugin, player.getUniqueId())), 2L);
        });

        setItem(51, new ItemBuilder(Material.EMERALD).setName("§aAuktion starten").lore(
                "§7Starte eine neue Auktion"
        ), event -> {
            Player player = (Player) event.getWhoClicked();
            player.closeInventory();
            AnvilGUI.Builder builder = new AnvilGUI.Builder()
                    .plugin(plugin)
                    .disableGeyserCompat()
                    .text("Preis eingeben")
                    .title("Auktionen: Erstellen")
                    .itemLeft(new ItemBuilder(Material.PAPER).lore("§7Gib den Preis ein, für den du dein Item verkaufen möchtest."))
                    .onClose(player1 -> Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getInventoryManager().openInventory(player, new AuctionHouseMainInventory(plugin)), 2L))
                    .onClick((integer, stateSnapshot) -> {
                        String itemName = stateSnapshot.getOutputItem().getItemMeta().getDisplayName();
                        if (itemName == null || itemName.isEmpty() || itemName.equalsIgnoreCase("Preis eingeben")) {
                            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst einen Preis eingeben.");
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        }
                        player.closeInventory();
                        try {
                            long price = Long.parseLong(itemName.replace(".", ""));
                            if (price < 1) {
                                player.sendMessage(SkyMythPlugin.PREFIX + "§cDer Preis muss mindestens 1.000 Tokens betragen.");
                                return Collections.singletonList(AnvilGUI.ResponseAction.close());
                            }
                            ItemStack itemStack = player.getInventory().getItemInHand();
                            if (itemStack == null || itemStack.getType() == Material.AIR) {
                                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst ein Item in der Hand halten.");
                                return Collections.singletonList(AnvilGUI.ResponseAction.close());
                            }
                            return Collections.singletonList(AnvilGUI.ResponseAction.run(() -> {
                                player.closeInventory();
                                AnvilGUI.Builder builder2 = new AnvilGUI.Builder()
                                        .plugin(plugin)
                                        .disableGeyserCompat()
                                        .text("Dauer eingeben")
                                        .title("Auktionen: Erstellen")
                                        .itemLeft(new ItemBuilder(Material.PAPER).lore("§7Gib die Dauer ein, für die du dein Item verkaufen möchtest."))
                                        .onClose(player1 -> Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getInventoryManager().openInventory(player, new AuctionHouseMainInventory(plugin)), 2L))
                                        .onClick((integer1, stateSnapshot1) -> {
                                            long duration = TimeUtil.parseTimeFromString(stateSnapshot1.getOutputItem().getItemMeta().getDisplayName());
                                            if (Duration.ofMillis(duration).toMinutes() < 15) {
                                                player.sendMessage(SkyMythPlugin.PREFIX + "§cDie Mindestdauer beträgt 15 Minuten.");
                                                return Collections.singletonList(AnvilGUI.ResponseAction.close());
                                            }
                                            player.closeInventory();
                                            return Collections.singletonList(AnvilGUI.ResponseAction.run(() -> {
                                                if (Duration.ofMillis(duration).toHours() >= 3) {
                                                    long fee = (long) (price * 0.05);
                                                    User user = plugin.getUserManager().getUser(player.getUniqueId());
                                                    if (user.getBalance() < fee) {
                                                        player.sendMessage(SkyMythPlugin.PREFIX + "§cDu hast nicht genügend Tokens um die Gebühr zu zahlen.");
                                                        player.sendMessage(SkyMythPlugin.PREFIX + "§cDie Gebühr für die Dauer wäre " + NumberFormat.getInstance(Locale.GERMAN).format(fee).replace(",", ".") + " Tokens.");
                                                        return;
                                                    }
                                                    user.removeBalance(fee);
                                                    plugin.getUserManager().saveUser(user);
                                                    AuctionHouseItem auctionHouseItem = new AuctionHouseItem(
                                                            RandomUtil.randomInt(100000, 999999),
                                                            player.getUniqueId(),
                                                            itemStack.clone(),
                                                            false,
                                                            price,
                                                            null,
                                                            System.currentTimeMillis(),
                                                            Duration.ofMillis(duration)
                                                    );
                                                    plugin.getAuctionHouseManager().addAuctionHouseItem(auctionHouseItem);
                                                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast dein Item für §e" + NumberFormat.getInstance(Locale.GERMAN).format(price).replace(",", ".") + " Tokens §7zum Verkauf angeboten.");
                                                    player.closeInventory();
                                                    Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getInventoryManager().openInventory(player, new AuctionHouseMainInventory(plugin)), 2L);
                                                }
                                            }));
                                        });
                                Bukkit.getScheduler().runTaskLater(plugin, () -> builder2.open(player), 2L);
                            }));
                        } catch (NumberFormatException exception) {
                            player.sendMessage(SkyMythPlugin.PREFIX + "§cUngültiger Preis.");
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        }
                    });
            Bukkit.getScheduler().runTaskLater(plugin, () -> builder.open(player), 2L);
        });

        setItem(53, new ItemBuilder(Material.ARROW).setName("§7Nächste Seite").lore(
                "§7Klicke hier um zur nächsten Seite zu gelangen"
        ), event -> {
            if (this.pagination.isLastPage(this.page)) {
                return;
            }
            update(this.page + 1);
        });

        setItem(45, new ItemBuilder(Material.ARROW).setName("§7Vorherige Seite").lore(
                "§7Klicke hier um zur vorherigen Seite zu gelangen"
        ), event -> {
            if (this.pagination.isFirstPage(this.page)) {
                return;
            }
            update(this.page - 1);
        });

        int slot = 10;
        for (AuctionHouseItem auctionHouseItem : this.pagination.getItems(this.page)) {
            ItemBuilder itemBuilder = new ItemBuilder(auctionHouseItem.getItemStack());
            itemBuilder.setName(auctionHouseItem.getItemStack().getItemMeta().hasDisplayName() ? auctionHouseItem.getItemStack().getItemMeta().getDisplayName() : auctionHouseItem.getItemStack().getType().name());
            itemBuilder.lore(
                    "§7Verkäufer: §e" + UUIDFetcher.getName(auctionHouseItem.getSeller()),
                    "§7Preis: §e" + NumberFormat.getInstance(Locale.GERMAN).format(auctionHouseItem.getPrice()).replace(",", ".") + " Coins",
                    "§7Kategorie: §e" + (auctionHouseItem.getCategory() == null ? plugin.getAuctionHouseManager().determineCategory(auctionHouseItem).getName() : auctionHouseItem.getCategory().getName()),
                    "§7Ablaufdatum: §e" + TimeUtil.beautifyTime(auctionHouseItem.getRemainingTime(), TimeUnit.MILLISECONDS, true, true)
            );
            setItem(slot, itemBuilder, event -> {
                Player player = (Player) event.getWhoClicked();
                User user = plugin.getUserManager().getUser(player.getUniqueId());
                if (auctionHouseItem.getSeller().equals(player.getUniqueId())) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast deine Auktion entfernt.");
                    player.playSound(player.getLocation(), org.bukkit.Sound.ORB_PICKUP, 1, 1);
                    plugin.getAuctionHouseManager().removeAuctionHouseItem(auctionHouseItem);
                    player.getInventory().addItem(auctionHouseItem.getItemStack());
                    player.closeInventory();
                    Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getInventoryManager().openInventory(player, new AuctionHouseMainInventory(plugin)), 2L);
                    return;
                }
                if (auctionHouseItem.isExpired() || auctionHouseItem.getExpired()) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDiese Auktion ist bereits abgelaufen.");
                    player.playSound(player.getLocation(), org.bukkit.Sound.ORB_PICKUP, 1, 1);
                    update(page);
                    return;
                }
                if (user.getBalance() < auctionHouseItem.getPrice()) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu hast nicht genügend Tokens.");
                    player.playSound(player.getLocation(), org.bukkit.Sound.ORB_PICKUP, 1, 1);
                    return;
                }
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast die Auktion für §e" + NumberFormat.getInstance(Locale.GERMAN).format(auctionHouseItem.getPrice()).replace(",", ".") + " Tokens §7gekauft.");
                player.playSound(player.getLocation(), org.bukkit.Sound.LEVEL_UP, 1, 1);
                user.removeBalance(auctionHouseItem.getPrice());
                plugin.getUserManager().saveUser(user);
                plugin.getAuctionHouseManager().removeAuctionHouseItem(auctionHouseItem);
                player.getInventory().addItem(auctionHouseItem.getItemStack());
                update(page);

                Player seller = plugin.getServer().getPlayer(auctionHouseItem.getSeller());
                if (seller == null) return;
                seller.sendMessage(SkyMythPlugin.PREFIX + "§7Deine Auktion wurde für §e" + NumberFormat.getInstance(Locale.GERMAN).format(auctionHouseItem.getPrice()).replace(",", ".") + " Tokens §7verkauft.");
                seller.sendMessage(SkyMythPlugin.PREFIX + "§7Käufer: §e" + player.getName());
            });
            slot++;
            if (slot == 17) {
                slot = 19;
            } else if (slot == 26) {
                slot = 28;
            } else if (slot == 35) {
                slot = 37;
            }
        }
    }
}
