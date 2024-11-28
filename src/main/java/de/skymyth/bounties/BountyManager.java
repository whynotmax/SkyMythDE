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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<Bounty> getBounties(UUID target) {
        return this.bounties.stream().filter(bounty -> bounty.getTarget().equals(target)).collect(Collectors.toList());
    }

    public void saveBounties(List<Bounty> bounties) {
        this.bounties.addAll(bounties);
        this.repository.saveAll(bounties);
    }

    public void removeBounty(Bounty bounty) {
        this.bounties.remove(bounty);
        this.repository.delete(bounty);
    }

    public Map<UUID, Long> sortedBounties() {
        //Sort bounties by reward. If two bounties have the same target, the rewards will be added together.
        return this.bounties.stream().collect(Collectors.groupingBy(Bounty::getTarget, Collectors.summingLong(Bounty::getReward)))
                .entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
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
