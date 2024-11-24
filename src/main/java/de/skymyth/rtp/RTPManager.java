package de.skymyth.rtp;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.RandomUtil;
import de.skymyth.utility.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Random;

public class RTPManager {

    private final SkyMythPlugin plugin;
    private final Player player;
    private final World world;
    private int xCoord = -1;
    private int zCoord = -1;
    private int xF;
    private int yF;
    private int zF;

    public RTPManager(SkyMythPlugin plugin, Player player, World world, int xCoord, int zCoord) {
        this.plugin = plugin;
        this.player = player;
        this.world = world;
        this.xCoord = xCoord;
        this.zCoord = zCoord;
    }

    public void teleport() {
        Location location = this.getLocation();
        if (location == null) {
            this.player.sendMessage(ChatColor.RED + "ERROR: Failed to find a safe teleport location!");
        } else {
            this.player.teleport(location);
        }
    }

    public int getX() {
        return this.xF;
    }

    public int getY() {
        return this.yF;
    }

    public int getZ() {
        return this.zF;
    }

    private void set(double x, double y, double z) {
        this.xF = (int) x;
        this.yF = (int) y;
        this.zF = (int) z;
    }

    protected Location getLocation() {
        Random random = Util.RANDOM;
        int x = random.nextInt(this.xCoord);
        int z = random.nextInt(this.zCoord);
        x = this.randomizeType(x);
        z = this.randomizeType(z);
        int y = 63;
        Location location = RandomUtil.safeizeLocation(new Location(this.world, (double) x, (double) y, (double) z));
        if (location == null) {
            return null;
        } else {

            this.set(location.getX(), location.getY(), location.getZ());
            return location;
        }
    }

    protected int randomizeType(int i) {
        int j = new Random().nextInt(2);
        return switch (j) {
            case 0 -> -i;
            case 1 -> i;
            default -> -1;
        };
    }

}
