package de.skymyth.commands.impl;

import com.avaje.ebeaninternal.server.cache.DefaultServerCache;
import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.user.model.User;
import de.skymyth.user.model.cooldown.Cooldown;
import de.skymyth.utility.TimeUtil;
import de.skymyth.utility.Util;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReportCommand extends MythCommand {

    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public enum ReportReason {


        HACKING("Cheating"),
        BUGABUSING("Bugabusing"),
        RACISM("Rasissmus"),
        NAME_OR_SKIN("Name oder Skin"),
        BEHAVIOR("Verhalten");

        String displayName;
    }

    public ReportCommand(SkyMythPlugin plugin) {
        super("report", null, List.of("melden", "meldung"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        if(args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            String reason = args[1];

            for (ReportReason value : ReportReason.values()) {
                if(reason.toLowerCase().equalsIgnoreCase(value.displayName)) {
                    reason = value.displayName;
                }
            }


            if(target == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler ist nicht online.");
                return;
            }

            if(target == player) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst dich selber nicht reporten.");
                return;
            }

            if(reason != null) {

                User user = plugin.getUserManager().getUser(player.getUniqueId());

                if (user.isOnCooldown("command_report")) {
                    long remaining = user.getCooldown("command_report").getRemainingTime();
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst noch " +
                            TimeUtil.beautifyTime(remaining, TimeUnit.MILLISECONDS, true, true)
                            + " warten, bevor du den Befehl erneut verwenden kannst.");
                    return;
                }

                Cooldown cooldown = new Cooldown();
                cooldown.setName("command_report");
                cooldown.setDuration(Duration.ofMinutes(5));
                cooldown.start();
                user.addCooldown(cooldown);

                plugin.getUserManager().saveUser(user);

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if(!onlinePlayer.hasPermission("skymyth.team")) continue;
                    onlinePlayer.sendMessage(SkyMythPlugin.PREFIX + "§c" + target.getName() + " §7wurde von §c" + player.getName() + " §7für §c" + reason + " §7gemeldet.");
                }
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Dein Report wurde erfolgreich abgesendet, Danke!");
                return;

            }


            return;
        }
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /report <spieler> <grund>");



    }

}
