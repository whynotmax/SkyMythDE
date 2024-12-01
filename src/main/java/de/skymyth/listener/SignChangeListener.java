package de.skymyth.listener;

import de.skymyth.SkyMythPlugin;
import de.skymyth.freesigns.model.FreeSign;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public record SignChangeListener(SkyMythPlugin plugin) implements Listener {

     @EventHandler
     public void onSignChange(SignChangeEvent event) {
         Block block = event.getBlock();
         Player player = event.getPlayer();
         if (!player.hasPermission("skymyth.freesign.create")) {
             return;
         }
         if (player.getItemInHand().getType() == Material.AIR) {
             return;
         }
         if (event.getLine(0).contains("[free]")) {
             FreeSign sign = plugin.getFreeSignManager().create(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ()));
             sign.setLocation(block.getLocation());
             sign.setItemStack(new ItemBuilder(player.getItemInHand().clone()).amount(1));
             event.setLine(0, "§r");
             event.setLine(1, "§8[§5SkyMyth Free§8]");
             event.setLine(2, "§81x " + player.getItemInHand().getType().name().replace("_", " ").substring(0, (Math.min(player.getItemInHand().getType().name().length(), 10))));
             event.setLine(3, "§r");
             plugin.getFreeSignManager().save(sign);
             player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast ein FreeSign erstellt. ID: §e" + sign.getId());
         }
     }
}
