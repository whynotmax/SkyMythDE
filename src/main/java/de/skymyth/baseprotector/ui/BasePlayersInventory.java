package de.skymyth.baseprotector.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.baseprotector.model.BaseProtector;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.utility.UUIDFetcher;
import de.skymyth.utility.Util;
import de.skymyth.utility.item.ItemBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class BasePlayersInventory extends AbstractInventory {

    SkyMythPlugin plugin;
    Player player;
    BaseProtector baseProtector;

    public BasePlayersInventory(SkyMythPlugin plugin, Player player, BaseProtector baseProtector) {
        super("Basisschutz: Mitbauer", 36);
        this.plugin = plugin;
        this.player = player;
        this.baseProtector = baseProtector;

        this.defaultInventory();

        AtomicInteger slotCounter = new AtomicInteger(10);
        for (UUID trustedPlayer : baseProtector.getTrustedPlayers()) {

            String name = UUIDFetcher.getName(trustedPlayer);

            if (slotCounter.get() == 17) {
                slotCounter.set(19);
            }

            if (slotCounter.get() > 25) return;

            ItemBuilder skullItem = new ItemBuilder(Material.SKULL_ITEM)
                    .skullOwner(name)
                    .setName("§a" + name);

            if (this.baseProtector.getBaseOwner().equals(player.getUniqueId())) {
                skullItem.lore(
                        "",
                        "§cKlicke, um diesen Spieler zu entfernen"
                );
            }
            setItem(slotCounter.getAndIncrement(), skullItem, event -> {

                if (this.baseProtector.getBaseOwner().equals(player.getUniqueId())) {
                    String playerName = this.getInventory().getItem(event.getRawSlot()).getItemMeta().getDisplayName()
                            .replaceAll("§a", "")
                            .trim();

                    UUID uuid = UUIDFetcher.getUUID(playerName);
                    Player target = Bukkit.getPlayer(uuid);

                    this.baseProtector.getTrustedPlayers().remove(uuid);
                    plugin.getBaseProtectorManager().saveBaseProtection(baseProtector);
                    player.sendMessage(SkyMythPlugin.PREFIX + "§c" + playerName + " wurde als Mitbauer entfernt.");

                    if (target != null) {
                        target.sendMessage(SkyMythPlugin.PREFIX + "§cDu wurdest als Mitbauer von " + Bukkit.getOfflinePlayer(baseProtector.getBaseOwner()).getName() + "'s Basisschutz entfernt.");
                    }

                    player.closeInventory();
                    plugin.getInventoryManager().openInventory(player, new BasePlayersInventory(plugin, player, baseProtector));

                }
            });

        }

        setItem(31, new ItemBuilder(Material.NAME_TAG)
                .setName("§aNeuen Spieler einladen")
                .lore(
                        "",
                        "§cKlicke, um einen neuen Spieler einzuladen"
                ), event -> {
            AnvilGUI.Builder anvilGui = new AnvilGUI.Builder()
                    .plugin(plugin)
                    .disableGeyserCompat()
                    .text("Spielername eingeben")
                    .title("Basisschutz: Einladung")
                    .onClose(player1 -> plugin.getInventoryManager().openInventory(player, new BasePlayersInventory(plugin, player1.getPlayer(), baseProtector)))
                    .onClick((integer, stateSnapshot) -> {
                        String itemName = stateSnapshot.getOutputItem().getItemMeta().getDisplayName();
                        if (itemName == null || itemName.isEmpty() || itemName.equalsIgnoreCase("Spielername eingeben")) {
                            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst einen Spielernamen eingeben.");
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        }
                        player.closeInventory();

                        String playerName = itemName;
                        Player target = Bukkit.getPlayer(playerName);

                        if (target == null) {
                            player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler wurde nicht gefunden.");
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Nicht online"));
                        }

                        if (target == player) {
                            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst dich selber nicht einladen.");
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Nicht gültig"));
                        }

                        if (Util.BASEINVITE.asMap().containsKey(target)) {
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Spieler bereits eingeladen"));
                        }
                        return Collections.singletonList(AnvilGUI.ResponseAction.run(() -> {
                            Util.BASEINVITE.put(target, this.baseProtector);
                            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + target.getName() + " §7in deinen Basisschutz eingeladen.");
                            target.sendMessage(SkyMythPlugin.PREFIX + "§7Du wurdest in den Basisschutz von §e" + player.getName() + " §7eingeladen.");
                            target.sendMessage(SkyMythPlugin.PREFIX + "§7Nimm die Einladung mit §e/Base accept §7an oder lass sie ablaufen.");
                            player.closeInventory();
                        }));
                    });
            anvilGui.open(player);
        });

        setItem(27, new ItemBuilder(Material.BARRIER).setName("§cZurück"), inventoryClickEvent -> plugin.getInventoryManager().openInventory(player, new BaseMainInventory(player, plugin)));
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
