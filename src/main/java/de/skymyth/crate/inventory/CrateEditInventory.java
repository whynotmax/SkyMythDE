package de.skymyth.crate.inventory;

import de.skymyth.SkyMythPlugin;
import de.skymyth.crate.model.Crate;
import de.skymyth.crate.model.CrateItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public record CrateEditInventory(SkyMythPlugin plugin) implements Listener {


    public Inventory getCrateEditInventory(Crate crate) {
        Inventory inventory = Bukkit.createInventory(null, 54, crate.getName() + " - Edit");

        int i = 0;
        for (CrateItem crateItem : crate.getCrateItems()) {
            inventory.setItem(i, crateItem.getItemStack());
            i++;
        }
        return inventory;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if(event.getPlayer() instanceof Player player) {
            if (event.getInventory().getName().contains("- Edit")) {
                String crateName = event.getInventory().getName().trim().replaceAll(" - Edit", "");
                Crate crate = plugin.getCrateManager().getCrate(crateName);

                if (crate == null) return;
                if (!player.hasPermission("myth.crates")) return;

                for (ItemStack content : event.getInventory().getContents()) {
                    if(content.getType() == Material.AIR) return;
                }
            }
        }
    }
}
