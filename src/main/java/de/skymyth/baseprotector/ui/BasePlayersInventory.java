package de.skymyth.baseprotector.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.baseprotector.model.BaseProtector;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.utility.UUIDFetcher;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class BasePlayersInventory extends AbstractInventory {

    Player player;
    BaseProtector baseProtector;

    public BasePlayersInventory(Player player, BaseProtector baseProtector) {
        super("Basisschutz: Mitbauer", 37);
        this.player = player;
        this.baseProtector = baseProtector;

        this.defaultInventory();

        AtomicInteger slotCounter = new AtomicInteger(10);
        for (UUID trustedPlayer : baseProtector.getTrustedPlayers()) {

            String name = UUIDFetcher.getName(trustedPlayer);

            if (slotCounter.get() == 16) {
                slotCounter.set(19);
            }

            if (slotCounter.get() > 25) return;

            ItemBuilder skullItem = new ItemBuilder(Material.SKULL_ITEM)
                    .skullOwner(name)
                    .setName("§a" + name);

            if(this.baseProtector.getBaseOwner().equals(player.getUniqueId())) {
                skullItem.lore(
                        "",
                        "§cKlicke, um diesen Spieler zu entfernen"
                );
            }
            setItem(slotCounter.getAndIncrement(), skullItem, event -> {

                if(this.baseProtector.getBaseOwner().equals(player.getUniqueId())) {
                    String playerName = this.getInventory().getItem(event.getRawSlot()).getItemMeta().getDisplayName()
                            .replaceAll("§", "")
                            .trim();

                    UUID uuid = UUIDFetcher.getUUID(playerName);

                    this.baseProtector.getTrustedPlayers().remove(uuid);
                    player.sendMessage(SkyMythPlugin.PREFIX + "§c" + playerName + " wurde als Mitbauer entfernt.");
                }
            });

        }

        setItem(31, new ItemBuilder(Material.NAME_TAG)
                .setName("§aNeuen Spieler einladen")
                .lore(
                        "",
                        "§cKlicke, um einen neuen Spieler einzuladen"
                ), event -> {
            player.sendMessage("nö");
        });
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
