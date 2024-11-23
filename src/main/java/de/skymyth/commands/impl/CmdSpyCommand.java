package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.Util;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdSpyCommand extends MythCommand {

    public CmdSpyCommand(SkyMythPlugin plugin) {
        super("cmdspy", "myth.cmdspy", List.of("commandspy"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (Util.MSGSPY.contains(player)) {
            Util.MSGSPY.remove(player);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast den CommandSpy §cdeaktiviert.");
            return;
        }
        Util.MSGSPY.add(player);
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast den CommandSpy §aaktiviert.");
    }
}
