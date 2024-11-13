package de.skymyth.combat;

import de.skymyth.SkyMythPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class LogoutListener implements Listener {

    public static ArrayList<Player> combat = new ArrayList<>();

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        Player p = e.getPlayer();

        if(combat.contains(p)) {
            p.setHealth(0);
            Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§e" + p.getName() + " §7hat sich im Kampf ausgeloggt§8.");
            combat.remove(p);
        }

    }

}
