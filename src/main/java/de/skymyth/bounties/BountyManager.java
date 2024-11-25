package de.skymyth.bounties;

import de.skymyth.SkyMythPlugin;
import de.skymyth.bounties.model.Bounty;
import de.skymyth.bounties.repository.BountyRepository;
import de.skymyth.user.model.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BountyManager implements Listener {

    SkyMythPlugin plugin;
    BountyRepository repository;
    List<Bounty> bounties;

    public BountyManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.repository = plugin.getMongoManager().create(BountyRepository.class);
        this.bounties = repository.findAll();
    }

    public void saveBounty(Bounty bounty) {
        this.bounties.add(bounty);
        this.repository.save(bounty);
    }

    public Bounty getBounty(UUID target) {
        return this.bounties.stream().filter(bounty -> bounty.getTarget().equals(target)).findFirst().orElse(null);
    }

    public void saveBounties(List<Bounty> bounties) {
        this.bounties.addAll(bounties);
        this.repository.saveAll(bounties);
    }

    public void removeBounty(Bounty bounty) {
        this.bounties.remove(bounty);
        this.repository.delete(bounty);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (killer == null) {
            return;
        }

        Bounty bounty = this.getBounty(player.getUniqueId());
        if (bounty == null) {
            return;
        }
        long reward = bounty.getReward();
        if (reward == 0) {
            return;
        }

        User user = plugin.getUserManager().getUser(killer.getUniqueId());
        user.setBalance(user.getBalance() + reward);
        plugin.getUserManager().saveUser(user);

        bounty.getHunters().clear();
        this.removeBounty(bounty);

        for (Player pvpPlayer : Bukkit.getWorld("PvP").getPlayers()) {
            pvpPlayer.sendMessage(SkyMythPlugin.PREFIX + "§e" + killer.getName() + "§7 hat sich das Kopfgeld von §e" + player.getName() + " §7geholt und erhält §e" + reward + " Tokens§7.");
        }
    }

}
