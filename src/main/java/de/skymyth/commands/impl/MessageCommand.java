package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.punish.model.result.PunishCheckResult;
import de.skymyth.punish.model.type.PunishType;
import de.skymyth.utility.Util;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MessageCommand extends MythCommand {

    public MessageCommand(SkyMythPlugin plugin) {
        super("msg", null, new ArrayList<>() {{
            add("pm");
            add("tell");
        }}, plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        PunishCheckResult punishCheckResult = plugin.getPunishManager().check(player.getUniqueId());
        if (punishCheckResult.isPunished() && punishCheckResult.getPunish().getType() == PunishType.MUTE) {
            plugin.getPunishManager().sendMuteMessage(punishCheckResult.getPunish());
            return;
        }

        if (args.length > 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler wurde nicht gefunden.");
                return;
            }

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 1; i < args.length; i++) {
                stringBuilder.append(args[i]).append(" ");
            }

            if (target == player) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst dir selber nicht schreiben.");
                return;
            }

            player.sendMessage(SkyMythPlugin.PREFIX + "§eDu §7zu §e" + target.getName() + " §8» §7" + stringBuilder);
            player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
            target.sendMessage(SkyMythPlugin.PREFIX + "§e" + player.getName() + " §7zu §eDir §8» §7" + stringBuilder);
            target.playSound(target.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);

            for (Player messageSpyPlayer : Util.MSGSPY) {
                messageSpyPlayer.sendMessage(SkyMythPlugin.PREFIX + "§e" + player.getName() + " §7zu §e" + target.getName() + " §8» §7" + stringBuilder);
            }

            Util.MESSAGE.put(player, target);
            Util.MESSAGE.put(target, player);
            return;
        }

        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /msg <spieler> <nachricht>");
    }
}
