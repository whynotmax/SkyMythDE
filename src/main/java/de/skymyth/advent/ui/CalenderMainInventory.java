package de.skymyth.advent.ui;

import de.skymyth.SkyMythPlugin;
import de.skymyth.advent.model.AdventDay;
import de.skymyth.inventory.impl.AbstractInventory;
import de.skymyth.user.model.User;
import de.skymyth.utility.item.ItemBuilder;
import de.skymyth.utility.item.SkullCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class CalenderMainInventory extends AbstractInventory {

    private final SkyMythPlugin plugin;

    public CalenderMainInventory(SkyMythPlugin plugin, Player player) {
        super("Advent: Kalender", 54);
        this.plugin = plugin;

        this.defaultInventory();

        int month = 11; // November for development else set it to 12 for december

        User user = plugin.getUserManager().getUser(player.getUniqueId());
        LocalDate today = LocalDate.now();
        AtomicInteger slotCounter = new AtomicInteger(11);

        for (int i = 1; i <= 24; i++) {
            if (slotCounter.get() == 16) slotCounter.set(19);
            if (slotCounter.get() == 26) slotCounter.set(28);
            if (slotCounter.get() == 35) slotCounter.set(38);
            if (slotCounter.get() > 42) break;

            ItemStack skull = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI5MTljNjczMTdjNzY3ODQzOGZmNTIwYzk4ZGRlMGUzYjRkNjg3NjljODkzOGE1YTNkZTI5NjhlZGZjNzMxNCJ9fX0=");
            ItemBuilder itemBuilder = new ItemBuilder(skull)
                    .setName("§7Adventstürchen: §c" + i);

            boolean taken = user.getAdventDayOpened().getOrDefault(i, false);

            if (today.getMonthValue() == month && today.getDayOfMonth() == i) {
                if (taken) {
                    itemBuilder.lore(
                            "",
                            "§7Dieser Tag ist Heute! Hole das Türchen ab.",
                            "§7Es sind noch §c" + (24 - i) + " §7Tage bis §cWeihnachten!",
                            "",
                            "§cDu hast dieses Türchen schon abgeholt."
                    );
                } else {
                    itemBuilder.lore(
                            "",
                            "§7Dieser Tag ist Heute! Hole das Türchen ab.",
                            "§7Es sind noch §c" + (24 - i) + " §7Tage bis §cWeihnachten!",
                            "",
                            "§aKlicke, um die Belohnung abzuholen."

                    );
                }
            } else if (today.getMonthValue() == month && i < today.getDayOfMonth()) {
                if (taken) {
                    itemBuilder.lore(
                            "",
                            "§cDu hast dieses Türchen bereits geöffnet."
                    );
                } else {
                    itemBuilder.lore(
                            "",
                            "§cDu hast dieses Türchen leider verpasst."
                    );
                }
            } else {
                itemBuilder.lore(
                        "",
                        "§7Dieses Türchen kannst du in §c" + (i - today.getDayOfMonth()) + " Tagen §7abholen."
                );
            }

            setItem(slotCounter.getAndIncrement(), itemBuilder, event -> {
                String itemName = this.getInventory().getItem(event.getRawSlot()).getItemMeta().getDisplayName()
                        .replaceAll("§7Adventstürchen: §c", "")
                        .trim();
                int currentDay = Integer.parseInt(itemName);
                if (today.getMonthValue() == month && today.getDayOfMonth() == currentDay) {

                    boolean alreadyTaken = plugin.getUserManager().getUser(player.getUniqueId())
                            .getAdventDayOpened().getOrDefault(currentDay, false);

                    if(alreadyTaken) {
                        player.sendMessage(SkyMythPlugin.PREFIX + "§cDu hast dieses Türchen bereits abgeholt.");
                        return;
                    }

                    player.closeInventory();
                    plugin.getInventoryManager().openInventory(player, new CalenderMainInventory(plugin, player));

                    user.openAdvent(currentDay);
                    plugin.getUserManager().saveUser(user);

                    player.sendMessage(SkyMythPlugin.PREFIX + "§aDu hast das " + currentDay + ". Türchen geöffnet.");

                    AdventDay adventDay = plugin.getCalenderManager().getAdventDay(currentDay);

                    if(adventDay == null) {
                        player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Tag wurde nicht gesetzt.");
                        return;
                    }

                    if(!adventDay.getItemStacks().isEmpty()) {
                        for (ItemStack itemStack : adventDay.getItemStacks()) {
                            player.getInventory().addItem(itemStack);
                            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + itemStack.getAmount() + "§8x §e" +
                                    (itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : itemStack.getType()) + " §7erhalten.");
                        }
                    }

                    if(adventDay.getTokens() > 0) {
                        user.addBalance(adventDay.getTokens());
                        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + adventDay.getTokens() + " Tokens §7erhalten.");
                    }
                } else {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Tag ist heute nicht.");
                }
            });
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {
    }
}
