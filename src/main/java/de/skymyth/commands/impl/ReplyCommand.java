package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.Util;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ReplyCommand extends MythCommand {

    public ReplyCommand(SkyMythPlugin plugin) {
        super("r", null, new ArrayList<>(){{
            add("reply");
        }}, plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        StringBuilder stringBuilder = new StringBuilder();

        for (String arg : args) {
            stringBuilder.append(arg).append(" ");
        }

        Player target = Util.MESSAGE.get(player);

        if(target == null) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst keinem Spieler antworten.");
            return;
        }

        player.sendMessage(SkyMythPlugin.PREFIX + "§eDu §7zu §e" + target.getName() + " §8» §7" + stringBuilder);
        player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
        target.sendMessage(SkyMythPlugin.PREFIX + "§e" + player.getName() + " §7zu §eDir §8» §7" + stringBuilder);
        target.playSound(target.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
    }
}
