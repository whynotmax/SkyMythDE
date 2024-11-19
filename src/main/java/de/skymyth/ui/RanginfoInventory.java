package de.skymyth.ui;

import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.utility.item.ItemBuilder;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class RanginfoInventory extends AbstractInventory {

    Player player;

    public RanginfoInventory(Player player) {
        super("Ranginfo", 27);
        this.player = player;
        Group group = LuckPermsProvider.get().getGroupManager().getGroup(LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getPrimaryGroup());

        defaultInventory();

        setItem(11, new ItemBuilder(Material.LEATHER_HELMET)
                .setName("§8#§e1 §8× §2Elite")
                .lore(
                        "§r",
                        "§7Vorteile:",
                        "§8× /§7kit §8» §7Zugriff auf das Elite-Kit",
                        "§8× /§7block §8» §7Gebe dir Blöcke",
                        "§8× /§7fly §8» §7Selbsterklärend",
                        "§8× /§7heal §8» §7Heile dich",
                        "§8× /§7hat §8» §7Setze dir einen Hut auf",
                        "§8× /§7near §8» §7Finde Spieler in der Nähe",
                        "§8× /§7ec §8» §7Öffne deine Enderkiste überall",
                        "§8× /§7invsee §8» §7Sehe das Inventar deines Gegners",
                        "§8× /§7stack §8» §7Stacke deine Items",
                        "§8× /§7ptime §8» §7Ändere deine Zeit auf dem Server",
                        "§8× /§7feed §8» §7Füttre dich",
                        "§8× /§7workbench §8» §7Öffne eine Werkbank überall",
                        "§r",
                        "§7Du hast alle Vorteile der niedrigeren Ränge.",
                        "§r",
                        (group.getCachedData().getMetaData().getMetaValue("name") != null && group.getCachedData().getMetaData().getMetaValue("name").contains("elite") ? "§aDu besitzt diesen Rang." : "§cDu besitzt diesen Rang nicht.")
                )
        );

        setItem(12, new ItemBuilder(Material.CHAINMAIL_HELMET)
                .setName("§8#§e2 §8× §dSaphir")
                .lore(
                        "§r",
                        "§7Vorteile:",
                        "§8× /§7kit §8» §7Zugriff auf das Saphir-Kit",
                        "§8× /§7saphirreward §8» §7Jeden Tag ein Extra-Reward",
                        "§r",
                        "§7Du hast alle Vorteile der niedrigeren Ränge.",
                        "§r",
                        (group.getCachedData().getMetaData().getMetaValue("name") != null && group.getCachedData().getMetaData().getMetaValue("name").contains("saphir") ? "§aDu besitzt diesen Rang." : "§cDu besitzt diesen Rang nicht.")
                )
        );

        setItem(13, new ItemBuilder(Material.IRON_HELMET)
                .setName("§8#§e3 §8× §ePhantom")
                .lore(
                        "§r",
                        "§7Vorteile:",
                        "§8× /§7kit §8» §7Zugriff auf das Phantom-Kit",
                        "§8× /§7jackpot start §8» §7Starte einen Jackpot",
                        "§8× /§7werbung §8» §7Mache Werbung für deinen Shop",
                        "§8× /§7kopf §8» §7Erhalte Köpfe von Spielern",
                        "§8× /§7recipe §8» §7Sehe die Rezepte aller Items",
                        "§r",
                        "§7Du hast alle Vorteile der niedrigeren Ränge.",
                        "§r",
                        (group.getCachedData().getMetaData().getMetaValue("name") != null && group.getCachedData().getMetaData().getMetaValue("name").contains("phantom") ? "§aDu besitzt diesen Rang." : "§cDu besitzt diesen Rang nicht.")
                )
        );

        setItem(14, new ItemBuilder(Material.GOLD_HELMET)
                .setName("§8#§e4 §8× §cMeister")
                .lore(
                        "§r",
                        "§7Vorteile:",
                        "§8× /§7kit §8» §7Zugriff auf das Meister-Kit",
                        "§8× /§7speed §8» §7Werde schneller",
                        "§8× /§7mtp §8» §7Teleportiere dich zu Spielern",
                        "§8× /§7votekick §8» §7Starte einen Votekick",
                        "§r",
                        "§7Du hast alle Vorteile der niedrigeren Ränge.",
                        "§r",
                        (group.getCachedData().getMetaData().getMetaValue("name") != null && group.getCachedData().getMetaData().getMetaValue("name").contains("meister") ? "§aDu besitzt diesen Rang." : "§cDu besitzt diesen Rang nicht.")
                )
        );

        setItem(15, new ItemBuilder(Material.DIAMOND_HELMET)
                .setName("§8#§e5 §8× §5Myth")
                .lore(
                        "§r",
                        "§7Vorteile:",
                        "§8× /§7kit §8» §7Zugriff auf das Myth-Kit",
                        "§8× /§7rename §8» §7Setze den Namen eines Items",
                        "§8× /§7servergeschenk §8» §7Gebe ein kleines Geschenk an alle Online Spieler",
                        "§8× /§7myholo §8» §7Erstelle bis zu drei Hologramme für deine Basis",
                        "§8× /§7sign §8» §7Signiere Items",
                        "§8× /§7setjoinmessage §8» §7Setze deine Joinnachricht",
                        "§8× /§7setquitmessage §8» §7Setze deine Quitnachricht",
                        "§8× /§7repair §8» §7Repariere ein Item ohne Repair-Token",
                        "§r",
                        "§7Du hast alle Vorteile der niedrigeren Ränge.",
                        "§r",
                        (group.getCachedData().getMetaData().getMetaValue("name") != null && group.getCachedData().getMetaData().getMetaValue("name").contains("myth") ? "§aDu besitzt diesen Rang." : "§cDu besitzt diesen Rang nicht.")
                )
        );
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
