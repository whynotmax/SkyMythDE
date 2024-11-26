package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.pvp.model.PvPShopItems;
import de.skymyth.utility.TitleUtil;
import de.skymyth.utility.Util;
import de.skymyth.utility.item.ItemBuilder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;


public class TestCommand extends MythCommand {


    public TestCommand(SkyMythPlugin plugin) {
        super("test", "myth.op", new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        player.getInventory().addItem(plugin.getBaseProtectorManager().getBaseProtectorItem());
        player.getInventory().addItem(new ItemBuilder(Material.BOW).setName("§b§lSniper").durability(354));
    }
}
