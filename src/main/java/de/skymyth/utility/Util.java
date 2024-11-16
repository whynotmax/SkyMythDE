package de.skymyth.utility;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@UtilityClass
@Getter
public class Util {

    public static final Map<Player, Player> MESSAGE = new HashMap<>();
    public static final Cache<Player, Player> TELEPORT = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();
    public static final ArrayList<Player> VANISH = new ArrayList<>();
    public static final ArrayList<Player> FREEZE = new ArrayList<>();


    public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public static String christmasColor(String text) {
        String[] color = new String[]{"§c§l", "§f§l"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            builder.append(color[i % 2]).append(text.charAt(i));
        }
        return builder.toString();
    }

    public static boolean containBlock(Chunk chunk, Block block) {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    if (chunk.getBlock(x, y, z).getType() == block.getType()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
