package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;
import de.skymyth.user.model.cooldown.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;

public record EntityInteractListener(SkyMythPlugin plugin) implements Listener {

    @EventHandler
    public void onItemFrameClick(PlayerInteractAtEntityEvent event) {

        if (event.getRightClicked() instanceof ItemFrame) {

            ItemFrame itemFrame = (ItemFrame) event.getRightClicked();

            if (itemFrame != null) {

                if (itemFrame.getWorld().getName().equals("PvP")) {
                    ItemStack itemStack = itemFrame.getItem();
                    Player player = event.getPlayer();

                    Inventory inventory = Bukkit.createInventory(null, 1*9, "Gratis: " + itemStack.getType());


                    for (int i = 0; i < 9; i++) {
                        inventory.setItem(i, itemStack);

                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if(event.getPlayer() instanceof Player player) {
            if (event.getInventory().getName().contains("Gratis: ")) {

                User user = plugin.getUserManager().getUser(player.getUniqueId());

                Cooldown cooldown = new Cooldown();
                cooldown.setName("gratis:" + );
                cooldown.setDuration(Duration.ofMillis(this.cooldown.toMillis()));
                cooldown.start();
                user.addCooldown(cooldown);
                plugin.getUserManager().saveUser(user);            }
        }
    }
}
