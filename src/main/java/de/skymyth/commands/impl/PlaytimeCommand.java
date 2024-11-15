package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.user.model.User;
import de.skymyth.utility.TimeUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlaytimeCommand extends MythCommand {

    public PlaytimeCommand(SkyMythPlugin plugin) {
        super("playtime", null, new ArrayList<>() {{
            add("pt");
            add("spielzeit");
        }}, plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Deine Spielzeit: §e" + TimeUtil.beautifyTime(user.getPlayTime(), TimeUnit.MILLISECONDS, true, true));
    }
}
