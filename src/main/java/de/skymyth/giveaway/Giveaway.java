package de.skymyth.giveaway;

import de.skymyth.SkyMythPlugin;
import org.bukkit.entity.Player;

public abstract class Giveaway {

    protected SkyMythPlugin plugin;

    public Giveaway(SkyMythPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void run();

    public abstract Player determineWinner();

}
