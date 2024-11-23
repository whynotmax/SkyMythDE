package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import de.skymyth.utility.TextComponentBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.List;

public class VerifyCommand extends MythCommand {

    public VerifyCommand(SkyMythPlugin plugin) {
        super("verify", null, List.of("verifizieren"), plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        String verificationCode = plugin.getRedissonManager().getVerifyManager().getVerificationCode(player.getUniqueId());
        TextComponentBuilder textComponentBuilder = new TextComponentBuilder(SkyMythPlugin.PREFIX + "§7Dein Verifizierungscode:");
        textComponentBuilder.append(new TextComponent(" "));
        TextComponentBuilder verificationCodeComponent = new TextComponentBuilder("§e" + verificationCode);
        verificationCodeComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("§7Klicke um den Code zu kopieren.")}));
        verificationCodeComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, verificationCode + " <- Kopiere diesen Code."));
        textComponentBuilder.append(verificationCodeComponent.toTextComponent());

        player.spigot().sendMessage(textComponentBuilder.toTextComponent());
        plugin.getRedissonManager().getVerifyManager().addVerification(player.getUniqueId(), verificationCode);
    }
}
