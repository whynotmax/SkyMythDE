package de.skymyth.commands.impl;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class HubCommand extends MythCommand {

    public HubCommand(SkyMythPlugin plugin) {
        super("hub", null, List.of("hubschrauber", "lobby", "l", "leave"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("Fallback");

        player.sendMessage(SkyMythPlugin.PREFIX + "§7Du wirst zur §eLobby §7gesendet...");

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}
