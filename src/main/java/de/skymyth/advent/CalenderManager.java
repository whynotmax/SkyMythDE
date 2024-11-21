package de.skymyth.advent;

import de.skymyth.advent.model.AdventDay;
import de.skymyth.utility.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalenderManager {

    public Map<Integer, AdventDay> adventDayMap;

    public CalenderManager() {
        this.adventDayMap = new HashMap<>();

        AdventDay firstDay = new AdventDay();
        firstDay.setDay(1);
        firstDay.setItemStacks(List.of(new ItemBuilder(Material.GOLDEN_APPLE).amount(3)));
        firstDay.setTokens(50);

        AdventDay twentyOneDay = new AdventDay();
        twentyOneDay.setDay(21);
        twentyOneDay.setItemStacks(List.of(new ItemBuilder(Material.GOLDEN_APPLE).amount(3)));
        twentyOneDay.setTokens(150);

        this.adventDayMap.put(1, firstDay);
        this.adventDayMap.put(21, twentyOneDay);
    }

    public AdventDay getAdventDay(int day) {
        return this.adventDayMap.get(day);
    }


}
