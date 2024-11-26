package de.skymyth.giveaway;

import de.skymyth.SkyMythPlugin;
import de.skymyth.giveaway.model.Giveaway;
import de.skymyth.utility.Util;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Manages the giveaways for the SkyMyth plugin.
 */
@Getter
public class GiveawayManager {

    SkyMythPlugin plugin;
    Giveaway currentGiveaway;
    Queue<Giveaway> giveawayQueue;
    boolean giveawayRunning;
    BukkitTask runnable;
    boolean paused;

    /**
     * Constructs a new GiveawayManager.
     *
     * @param plugin the SkyMyth plugin instance
     */
    public GiveawayManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.currentGiveaway = null;
        this.giveawayQueue = new ConcurrentLinkedQueue<>();
        this.giveawayRunning = false;
        this.paused = false;

        this.startQueue();
    }

    public void pause() {
        this.paused = true;
    }

    public void resume() {
        this.paused = false;
    }

    /**
     * Adds a giveaway to the queue.
     *
     * @param giveaway the giveaway to add
     */
    public void addGiveawayToQueue(Giveaway giveaway) {
        this.giveawayQueue.add(giveaway);
    }

    /**
     * Clears the giveaway queue.
     */
    public void clearQueue() {
        this.giveawayQueue.clear();
    }

    /**
     * Gets the next giveaway in line.
     * This method does not remove the giveaway from the queue! Use {@link #clearQueue()} to remove all giveaways.
     *
     * @return the next giveaway in the queue
     */
    public Giveaway getNextInLine() {
        return this.giveawayQueue.element();
    }

    /**
     * Starts processing the giveaway queue.
     * This method schedules a repeating task that processes the queue.
     */
    private void startQueue() {
        Util.GLOBALMUTE = true;
        giveawayRunning = true;
        runnable = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (paused) {
                return;
            }
            if (giveawayQueue.isEmpty()) {
                giveawayRunning = false;
                Util.GLOBALMUTE = false;
                return;
            }
            if (currentGiveaway != null && !currentGiveaway.done()) {
                return;
            }
            Giveaway giveaway = giveawayQueue.poll();
            if (giveaway == null) {
                return;
            }
            currentGiveaway = giveaway;
            giveaway.run();
        }, 0L, 20L);

    }

}