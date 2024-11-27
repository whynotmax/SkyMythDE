package de.skymyth.advent;

import de.skymyth.SkyMythPlugin;
import de.skymyth.advent.model.AdventDay;
import de.skymyth.badge.model.Badge;
import de.skymyth.utility.Util;
import de.skymyth.utility.item.ItemBuilder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CalenderManager {

    SkyMythPlugin plugin;
    Map<Integer, AdventDay> adventDayMap;

    public CalenderManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.adventDayMap = new HashMap<>();


        for (int i = 1; i < 23; i++) {
            AdventDay day = new AdventDay();
            day.setDay(i);
            day.setItemStacks(List.of(new ItemBuilder(Material.GOLDEN_APPLE).setDataId(1).amount(3)));
            day.setTokens(100);
            this.adventDayMap.put(i, day);
        }


        AdventDay lastDay = new AdventDay();
        lastDay.setDay(24);
        lastDay.setItemStacks(List.of(new ItemBuilder(Material.GOLDEN_APPLE).amount(16)));
        lastDay.setItemStacks(List.of(new ItemBuilder(Material.GOLDEN_APPLE).setDataId(1).amount(3)));
        lastDay.setActions(player -> {
            Badge badge = plugin.getBadgeManager().getBadge("weihnachten24");
            badge.getOwners().add(player.getUniqueId());
            plugin.getBadgeManager().saveBadge(badge);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast das " + Util.christmasColor("Weihnachten") + " 2024 " + "§8[§e" + badge.getColor() + badge.getCharacter() + "§8] §7Badge" + " erhalten!");

            Random random = Util.RANDOM;

            if(random.nextInt(100) >= 10) {
                player.getInventory().addItem(new ItemBuilder(Material.BOW).setName("§b§lSniper").durability(334));
            }
        });
        lastDay.setTokens(5000);
        this.adventDayMap.put(24, lastDay);

    }

    public AdventDay getAdventDay(int day) {
        return this.adventDayMap.get(day);
    }


}
