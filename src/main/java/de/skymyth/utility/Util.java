package de.skymyth.utility;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.skymyth.baseprotector.model.BaseProtector;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@UtilityClass
@Getter
public class Util {

    public static final Map<Player, Player> MESSAGE = new HashMap<>();
    public static final Cache<Player, Player> TELEPORT = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();
    public static final Cache<Player, BaseProtector> BASEINVITE = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();
    public static final ArrayList<Player> VANISH = new ArrayList<>();
    public static final ArrayList<Player> COMMANDWATCHER = new ArrayList<>();
    public static final ArrayList<Player> MSGSPY = new ArrayList<>();
    public static final ArrayList<Player> FREEZE = new ArrayList<>();
    public static final Random RANDOM = new Random();
    public static final List<String> BLOCKED_COMMANDS = new ArrayList<>() {{
        add("/plugins");
        add("/pl");
        add("/me");
        add("/say");
        add("/minecraft:");
        add("/ver");
        add("/version");
        add("/about");
        add("/icanhasbukkit");
        add("/bukkit:");
        add("/tell");
        add("/help");
        add("/?");
    }};
    public static boolean CANDROPITEMS = true;

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

    public static boolean isNumeric(final String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static void removeItem(Player player, ItemStack itemStack) {
        if (itemStack.getAmount() == 1) {
            player.getInventory().removeItem(itemStack);
        } else {
            itemStack.setAmount(itemStack.getAmount() - 1);
            player.updateInventory();
        }
    }

    public static HoverEvent showItem(ItemStack itemStack) {
        return new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(CraftItemStack.asNMSCopy(itemStack).save(new NBTTagCompound()).toString())});
    }


    public static Map<String, List<Block>> makeCircle(Location loc, int r) {
        List<Block> circleBlocks = new ArrayList<>();
        List<Block> innerBlocks = new ArrayList<>();
        int centerX = loc.getBlockX();
        int centerY = loc.getBlockY();
        int centerZ = loc.getBlockZ();
        World world = loc.getWorld();

        for (int x = centerX - r; x <= centerX + r; x++) {
            for (int z = centerZ - r; z <= centerZ + r; z++) {
                double distanceSquared = Math.pow(x - centerX, 2) + Math.pow(z - centerZ, 2);

                if (distanceSquared >= Math.pow(r - 0.5, 2) && distanceSquared <= Math.pow(r + 0.5, 2)) {
                    circleBlocks.add(world.getBlockAt(x, centerY, z));
                } else if (distanceSquared < Math.pow(r, 2)) {
                    innerBlocks.add(world.getBlockAt(x, centerY, z));
                }
            }
        }

        Map<String, List<Block>> result = new HashMap<>();
        result.put("circle", circleBlocks);
        result.put("inner", innerBlocks);

        return result;
    }

    public double getProcessCpuLoad()  {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
            AttributeList list = mbs.getAttributes(name, new String[]{"ProcessCpuLoad"});
            if (list.isEmpty()) {
                return Double.NaN;
            } else {
                Attribute att = (Attribute) list.get(0);
                Double value = (Double) att.getValue();
                return value != -1.0D ? (double) ((int) (value * 1000.0D)) / 10.0D : Double.NaN;
            }
        }catch (Exception exception) {
            return 0.0;
        }
    }


}
