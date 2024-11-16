package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.user.model.User;
import org.bukkit.entity.Player;

import java.util.List;

public class SetQuitMessageCommand extends MythCommand {

    public SetQuitMessageCommand(SkyMythPlugin plugin) {
        super("setquitmessage", "myth.myth", List.of("quitmessage"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /setquitmessage <clear/Nachricht>");
            return;
        }
        if (args[0].equalsIgnoreCase("clear")) {
            User user = plugin.getUserManager().getUser(player.getUniqueId());
            user.setQuitMessage(null);
            plugin.getUserManager().saveUser(user);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Deine Quit-Nachricht wurde gelöscht.");
            return;
        }
        String message = String.join(" ", args);
        if (!message.contains("%player%")) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Deine Nachricht muss den Platzhalter §e%player% §7enthalten.");
            return;
        }
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        user.setQuitMessage(message.replace("%player%", player.getName()));
        plugin.getUserManager().saveUser(user);
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Deine Quit-Nachricht wurde gesetzt.");
    }
}
