package de.skymyth.pvp;

import de.skymyth.SkyMythPlugin;
import de.skymyth.pvp.ui.PvPShopMainInventory;
import de.skymyth.utility.item.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PvPShopManager implements Listener {

    SkyMythPlugin plugin;
    Location location;
    ArmorStand armorStand;

    public PvPShopManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.location = plugin.getLocationManager().getPosition("pvp-shop-npc").getLocation();

        Bukkit.getPluginManager().registerEvents(this, plugin);

        for (Entity entity : this.location.getWorld().getEntities()) {
            if (entity.getType() != EntityType.ARMOR_STAND) continue;
            if (entity.getLocation().distance(plugin.getLocationManager().getPosition("pvp-shop-npc").getLocation()) < 3) {
                entity.remove();
            }
        }

        armorStand = this.location.getWorld().spawn(plugin.getLocationManager().getPosition("pvp-shop-npc")
                .getLocation(), ArmorStand.class);

        armorStand.setGravity(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName("§8» §cKämpfer Shop §8«");
        armorStand.setBasePlate(false);
        armorStand.setArms(true);
        armorStand.setHelmet(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGI5MmQzMTQ5Y2Y2YjNjNDcwNDM1ZWJiZWZkMWFmNzRmMWVkZTRiZjYwZWY3OTExN2NiMWEyYjI3ZjQ5ZjI4NCJ9fX0="));

        armorStand.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        armorStand.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        armorStand.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ArmorStand armorStand) {
            if (armorStand == this.armorStand) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {

        Player player = event.getPlayer();

        if (event.getRightClicked() instanceof ArmorStand armorStand) {
            if (armorStand == this.armorStand) {

                if (!player.getWorld().getName().equals(this.location.getWorld().getName())) {
                    player.kickPlayer("§cUnable to interact with entity");
                    return;
                }
                plugin.getInventoryManager().openInventory(player, new PvPShopMainInventory(plugin.getUserManager().getUser(player.getUniqueId())));
            }
        }
    }

}
