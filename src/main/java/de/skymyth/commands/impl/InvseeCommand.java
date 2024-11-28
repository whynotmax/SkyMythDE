package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InvseeCommand extends MythCommand implements Listener {

    private final Map<Player, Player> invsee = new HashMap<>();

    public InvseeCommand(SkyMythPlugin plugin) {
        super("invsee", "myth.invsee", new ArrayList<>(), plugin);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (invsee.containsKey(player)) {
                Player target = invsee.get(player);

                if (event.getRawSlot() >= 54) {
                    return;
                }

                if (!player.hasPermission("myth.invsee.take")) {
                    event.setCancelled(true);
                    return;
                }

                ItemStack clickedItem = event.getCurrentItem();
                int slot = event.getRawSlot();

                if (slot < target.getInventory().getSize()) {
                    event.setCancelled(false);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        ItemStack currentItem = event.getView().getTopInventory().getItem(slot);
                        target.getInventory().setItem(slot, currentItem);
                        target.updateInventory();
                    }, 1L);
                }
            }
        }
    }

    @Override
    public void run(Player player, String[] args) {


        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler wurde nicht gefunden.");
                return;
            }

            Inventory inventory = Bukkit.createInventory(null, 54, "Inventar: " + target.getName());

            for (int i = 0; i < target.getInventory().getSize(); i++) {
                ItemStack itemStack = target.getInventory().getItem(i);
                if (itemStack != null) {
                    inventory.setItem(i, itemStack);
                }
            }

            for (int i = 36; i < 45; i++) {
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDataId(15).setName("§r"));
            }

            ItemStack helmet = (target.getInventory().getHelmet() == null ? new ItemStack(Material.BARRIER) : target.getInventory().getHelmet());
            ItemStack chestplate = (target.getInventory().getChestplate() == null ? new ItemStack(Material.BARRIER) : target.getInventory().getChestplate());
            ItemStack leggings = (target.getInventory().getLeggings() == null ? new ItemStack(Material.BARRIER) : target.getInventory().getLeggings());
            ItemStack boots = (target.getInventory().getBoots() == null ? new ItemStack(Material.BARRIER) : target.getInventory().getBoots());

            inventory.setItem(46, helmet);
            inventory.setItem(47, chestplate);
            inventory.setItem(48, leggings);
            inventory.setItem(49, boots);
            invsee.put(player, target);
            player.openInventory(inventory);
            return;
        }
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /invsee <spieler>");

    }
}
