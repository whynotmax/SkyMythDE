package de.skymyth.punish;

import de.skymyth.SkyMythPlugin;
import de.skymyth.punish.model.Punish;
import de.skymyth.punish.model.reason.PunishReason;
import de.skymyth.punish.model.result.PunishCheckResult;
import de.skymyth.punish.model.type.PunishType;
import de.skymyth.punish.repository.PunishRepository;
import de.skymyth.utility.StringUtil;
import de.skymyth.utility.TimeUtil;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PunishManager {

    SkyMythPlugin plugin;
    PunishRepository repository;

    List<Punish> punishes;

    public PunishManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.repository = plugin.getMongoManager().create(PunishRepository.class);
        this.punishes = repository.findAll();
    }

    public String getBanScreen(Punish punish) {
        return "§r\n" +
                "§cDu wurdest von SkyMyth.DE gebannt.\n" +
                "§r\n" +
                "§7Grund: §e" + punish.getReason().getName() + "\n" +
                "§7Dauer: §e" + TimeUtil.beautifyTime(punish.getReason().getPunishDuration().toMillis(), TimeUnit.MILLISECONDS, true, false) + "\n" +
                "§r\n" +
                "§7Verbleibende Zeit: §e" + TimeUtil.beautifyTime(punish.getRemaining(), TimeUnit.MILLISECONDS, true, false) + "\n" +
                "§r\n" +
                "§7ID: §e" + punish.getId() + "\n" +
                "§r\n" +
                "§7Du kannst einen Entbannungsantrag auf unserem Discord stellen.\n" +
                "§9§ndiscord.skymyth.de§r\n" +
                "§r";
    }

    public void sendMuteMessage(Punish punish) {
        Player player = plugin.getServer().getPlayer(punish.getTarget());
        if (player != null) {
            player.sendMessage("§r");
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du wurdest für §e" + TimeUtil.beautifyTime(punish.getReason().getPunishDuration().toMillis(), TimeUnit.MILLISECONDS, true, false) + " §7gemutet.");
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Grund: §e" + punish.getReason().getName());
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Verbleibende Zeit: §e" + TimeUtil.beautifyTime(punish.getRemaining(), TimeUnit.MILLISECONDS, true, false));
            player.sendMessage("§r");
        }
    }

    public Punish getPunish(UUID target, PunishType type) {
        return punishes.stream().filter(punish -> punish.getTarget().equals(target) && punish.getType() == type).findFirst().orElse(null);
    }

    public PunishCheckResult check(UUID userToCheck) {
        Punish ban = this.getPunish(userToCheck, PunishType.BAN);
        Punish mute = this.getPunish(userToCheck, PunishType.MUTE);

        if (ban != null) {
            return new PunishCheckResult(true, ban);
        }
        if (mute != null) {
            return new PunishCheckResult(true, mute);
        }
        return new PunishCheckResult(false, null);
    }

    public void ban(UUID target, PunishReason reason) {
        if (reason.getType() != PunishType.BAN) {
            throw new IllegalArgumentException("Reason is not a ban reason. Use mute method instead.");
        }
        Punish punish = new Punish();
        punish.setTarget(target);
        punish.setType(PunishType.BAN);
        punish.setReason(reason);
        punish.setStart(System.currentTimeMillis());

        String id = StringUtil.splitStringInMiddle(StringUtil.getRandomString(8), "-");
        punish.setId(id);

        punishes.add(punish);
        repository.save(punish);

        Player targetPlayer = plugin.getServer().getPlayer(target);
        if (targetPlayer != null) {
            targetPlayer.kickPlayer(this.getBanScreen(punish));
        }

        Bukkit.broadcastMessage("§r");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + Bukkit.getOfflinePlayer(target).getName() + " §7wurde soeben gesperrt.");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Grund: §e" + reason.getName());
        Bukkit.broadcastMessage("§r");
    }

    public void unban(UUID target) {
        Punish punish = this.getPunish(target, PunishType.BAN);
        if (punish == null) {
            return;
        }
        punishes.remove(punish);
        repository.delete(punish);

        Bukkit.broadcastMessage("§r");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + Bukkit.getOfflinePlayer(target).getName() + " §7wurde soeben entbannt.");
        Bukkit.broadcastMessage("§r");
    }

    public void mute(UUID target, PunishReason reason) {
        if (reason.getType() != PunishType.MUTE) {
            throw new IllegalArgumentException("Reason is not a mute reason. Use ban method instead.");
        }
        Punish punish = new Punish();
        punish.setTarget(target);
        punish.setType(PunishType.MUTE);
        punish.setReason(reason);
        punish.setStart(System.currentTimeMillis());

        String id = StringUtil.splitStringInMiddle(StringUtil.getRandomString(8), "-");
        punish.setId(id);

        punishes.add(punish);
        repository.save(punish);

        Player targetPlayer = plugin.getServer().getPlayer(target);
        if (targetPlayer != null) {
            targetPlayer.sendMessage(SkyMythPlugin.PREFIX + "§7Du wurdest für §e" + TimeUtil.beautifyTime(reason.getPunishDuration().toMillis(), TimeUnit.MILLISECONDS, true, false) + " §7gemutet.");
            targetPlayer.sendMessage(SkyMythPlugin.PREFIX + "§7Grund: §e" + reason.getName());
        }

        Bukkit.broadcastMessage("§r");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + Bukkit.getOfflinePlayer(target).getName() + " §7wurde soeben gemutet.");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Grund: §e" + reason.getName());
        Bukkit.broadcastMessage("§r");
    }

    public void unmute(UUID target) {
        Punish punish = this.getPunish(target, PunishType.MUTE);
        if (punish == null) {
            return;
        }
        punishes.remove(punish);
        repository.delete(punish);

        Bukkit.broadcastMessage("§r");
        Bukkit.broadcastMessage(SkyMythPlugin.PREFIX + "§7Der Spieler §e" + Bukkit.getOfflinePlayer(target).getName() + " §7wurde soeben entmutet.");
        Bukkit.broadcastMessage("§r");
    }

}
