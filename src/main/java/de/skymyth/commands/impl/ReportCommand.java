package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.user.model.User;
import de.skymyth.user.model.cooldown.Cooldown;
import de.skymyth.utility.TimeUtil;
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

    public ReportCommand(SkyMythPlugin plugin) {
        super("report", null, List.of("melden", "meldung"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            String reason = args[1];

            boolean validReason = false;

            for (ReportReason value : ReportReason.values()) {
                if (reason.equalsIgnoreCase(value.name)) {
                    reason = value.displayName;
                    validReason = true;
                    break;
                }
            }

            if (!validReason) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Wähle einen der Gründe aus:");
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Beispiel§8: §7/report <name> Hacking");
                for (ReportReason reportReason : ReportReason.values()) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Name§8: §e" + reportReason.getName() + " §8(§7" + reportReason.getDisplayName() + "§8)");
                }
                return;
            }


            if (target == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler ist nicht online.");
                return;
            }

            /*
            if (target == player) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst dich selber nicht reporten.");
                return;
            }

             */

            if (reason != null) {

                User user = plugin.getUserManager().getUser(player.getUniqueId());

                if (user.isOnCooldown("commandReport")) {
                    long remaining = user.getCooldown("commandReport").getRemainingTime();
                    player.sendMessage(SkyMythPlugin.PREFIX + "§cDu musst noch " +
                            TimeUtil.beautifyTime(remaining, TimeUnit.MILLISECONDS, true, true)
                            + " warten, bevor du den Befehl erneut verwenden kannst.");
                    return;
                }

                Cooldown cooldown = new Cooldown();
                cooldown.setName("commandReport");
                cooldown.setDuration(Duration.ofMinutes(5));
                cooldown.start();
                user.addCooldown(cooldown);

                plugin.getUserManager().saveUser(user);

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (!onlinePlayer.hasPermission("skymyth.team")) continue;
                    onlinePlayer.sendMessage(SkyMythPlugin.PREFIX + "§e" + target.getName() + " §7wurde von §e" + player.getName() + " §7für §c" + reason + " §7gemeldet.");
                }
                player.sendMessage(SkyMythPlugin.PREFIX + "§7Dein Report wurde erfolgreich abgesendet, Danke!");
                return;

            }
            return;
        }
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /report <spieler> <grund>");


    }

    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public enum ReportReason {


        HACKING("Clientmodifikationen", "Hacking"),
        BUGABUSING("Bugabusing", "Bugabusing"),
        RACISM("Rassismus", "Rassismus"),
        NAME_OR_SKIN("Name oder Skin", "Name"),
        BEHAVIOR("Verhalten (im Chat oä.)", "Verhalten");

        String displayName;
        String name; // report argument
    }

}
