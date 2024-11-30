package de.skymyth.pvp.punish;

import de.skymyth.SkyMythPlugin;
import de.skymyth.pvp.punish.model.PvPPunishment;
import de.skymyth.pvp.punish.repository.PvPPunishmentRepository;
import de.skymyth.utility.TimeUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PvPPunishManager {

    SkyMythPlugin plugin;
    PvPPunishmentRepository repository;
    List<PvPPunishment> punishments;

    public PvPPunishManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.repository = plugin.getMongoManager().create(PvPPunishmentRepository.class);
        this.punishments = new ArrayList<>(repository.findAll());
    }

    public void punish(UUID uniqueId, String reason, long duration) {
        PvPPunishment punishment = new PvPPunishment(uniqueId, reason, (System.currentTimeMillis() + duration));
        repository.save(punishment);
        punishments.add(punishment);

        Player player = plugin.getServer().getPlayer(uniqueId);
        if (player != null) {
            player.sendMessage("§r");
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du wurdest aus der PvP-Zone verbannt.");
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Grund: §e" + reason);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Dauer: §e" + (punishment.isPermanent() ? "Permanent" : TimeUtil.beautifyTime(duration, TimeUnit.MILLISECONDS, true, true)));
            player.sendMessage("§r");

            if (!player.getWorld().getName().equalsIgnoreCase("PvP") && !player.getWorld().getName().equalsIgnoreCase("FpsArena")) {
                return;
            }
            player.teleport(plugin.getLocationManager().getPosition("spawn").toBukkitLocation());
        }
    }

    public void unpunish(UUID uniqueId) {
        PvPPunishment punishment = getPunishment(uniqueId);
        if (punishment == null) {
            return;
        }
        repository.delete(punishment);
        punishments.remove(punishment);

        Player player = plugin.getServer().getPlayer(uniqueId);
        if (player != null) {
            player.sendMessage("§r");
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du wurdest aus der PvP-Zone entbannt.");
            player.sendMessage("§r");
        }
    }

    public PvPPunishment getPunishment(UUID uniqueId) {
        return punishments.stream().filter(punishment -> punishment.getUniqueId().equals(uniqueId)).filter(punishment -> !punishment.isExpired()).findFirst().orElse(null);
    }

    public boolean isPunished(UUID uniqueId) {
        return getPunishment(uniqueId) != null;
    }

}
