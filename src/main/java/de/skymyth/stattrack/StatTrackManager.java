package de.skymyth.stattrack;

import de.skymyth.SkyMythPlugin;
import de.skymyth.stattrack.enchant.EnchantWrapper;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;

public class StatTrackManager {

    SkyMythPlugin plugin;

    public StatTrackManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack addStatTrack(ItemStack itemStack) {
        if (itemStack.getType().name().contains("SWORD")) {
            throw new IllegalArgumentException("Item is not a sword!");
        }
        ItemBuilder itemBuilder = new ItemBuilder(itemStack);
        itemBuilder.addEnchant(EnchantWrapper.STAT_TRACK, 1);
        return itemBuilder.clone();
    }

}
