package de.skymyth.commands.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.skymyth.SkyMythPlugin;
import de.skymyth.clan.model.Clan;
import de.skymyth.commands.MythCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.swing.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ClanCommand extends MythCommand {

    private final Cache<Player, Clan> clanInvite = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES).build();


    public ClanCommand(SkyMythPlugin plugin) {
        super("clan", "", new ArrayList<>(), plugin);
    }

    @Override
    public void run(Player player, String[] args) {

        if (args.length == 2 && args[0].equalsIgnoreCase("create")) {

            String clanName = args[1];

            if (plugin.getClanManager().isInClan(player.getUniqueId())) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu befindest dich bereits in einem Clan.");
                return;
            }

            if (!clanName.matches("[A-Za-z0-9]+")) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cBitte verwende keine Sonderzeichen im Clannamen.");
                return;
            }

            if (clanName.length() < 2) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Clanname ist zu kurz.");
                return;
            }

            if (clanName.length() > 5) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Clanname ist zu lange.");
                return;
            }

            if (plugin.getClanManager().existsClan(clanName)) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Clanname ist bereits vergeben.");
                return;
            }

            plugin.getClanManager().createClan(player.getUniqueId(), clanName);
            player.sendMessage(SkyMythPlugin.PREFIX + "§eDein Clan " + clanName + " wurde erstellt.");
            return;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("delete")) {

            Clan clan = plugin.getClanManager().getClan(player.getUniqueId());

            if(!plugin.getClanManager().isInClan(player.getUniqueId())) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu befindest dich in keinem Clan.");
                return;
            }

            if (!clan.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cNur der Clanbesitzer kann den Clan löschen.");
                return;
            }

            for (UUID member : clan.getMembers()) {
                clan.getMembers().remove(member);

                Player clanPlayer = Bukkit.getPlayer(member);
                if (clanPlayer != null) {
                    clanPlayer.sendMessage(SkyMythPlugin.PREFIX + "§cDein Clan wurde von §e" + player.getUniqueId() + " §caufgelöst.");
                }
            }
            clan.setLeader(null);
            plugin.getClanManager().deleteClan(clan);
            player.sendMessage(SkyMythPlugin.PREFIX + "§eDein Clan wurde erfolgreich aufgelöst.");
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("invite")) {

            Clan clan = plugin.getClanManager().getClan(player.getUniqueId());
            String playerName = args[1];

            if(!plugin.getClanManager().isInClan(player.getUniqueId())) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu befindest dich in keinem Clan.");
                return;
            }

            if (!clan.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cNur der Clanbesitzer kann Spieler einladen.");
                return;
            }

            if(clan.getMembers().size() > clan.getMaxMembers()) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDein Clan ist voll §8(§a"+ clan.getMembers().size() + " §8/ §c" + clan.getMaxMembers() + "§8)");
                return;
            }

            Player targetPlayer = Bukkit.getPlayer(playerName);

            if(targetPlayer == player) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu kannst dich selber nicht einladen.");
                return;
            }

            if(targetPlayer == null || !targetPlayer.hasPlayedBefore()) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler war noch nie auf SkyMyth.");
                return;
            }

            if(this.clanInvite.getIfPresent(targetPlayer) != null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler hat bereits eine offene Claneinladung.");
                return;
            }

            if(plugin.getClanManager().isInClan(targetPlayer.getUniqueId())) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Spieler ist bereits in einem Clan.");
                return;
            }

            this.clanInvite.put(targetPlayer, clan);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du hast §e" + targetPlayer.getName() + " §7eine Claneinladung geschickt.");

            targetPlayer.sendMessage(SkyMythPlugin.PREFIX + "§7Du wurdest in den Clan §e" + clan.getName() + " §7eingeladen.");
            targetPlayer.sendMessage(SkyMythPlugin.PREFIX + "§7Trete dem Clan jetzt mit §a/Clan accept §7bei.");
            targetPlayer.sendMessage(SkyMythPlugin.PREFIX + "§cDie Anfrage läuft in 3 Minuten automatisch ab.");
            return;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("leave")) {

            if(!plugin.getClanManager().isInClan(player.getUniqueId())) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu befindest dich in keinem Clan.");
                return;
            }

            Clan clan = plugin.getClanManager().getClan(player.getUniqueId());

            if(clan.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cAls Besitzer kannst du deinen Clan nicht verlassen.");
                return;
            }

            if(!clan.getMembers().contains(player.getUniqueId())) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu bist kein Mitglied dieses Clans.");
                return;
            }

            for (UUID member : clan.getMembers()) {
                Player clanPlayer = Bukkit.getPlayer(member);
                if (clanPlayer != null) {
                    clanPlayer.sendMessage(SkyMythPlugin.PREFIX + "§e" + player.getName() + " §7hat deinen Clan §cverlassen.");
                }
            }
            Player leaderPlayer = Bukkit.getPlayer(clan.getLeader());

            if(leaderPlayer != null) {
                leaderPlayer.sendMessage(SkyMythPlugin.PREFIX + "§e" + player.getName() + " §7hat deinen Clan §cverlassen.");
            }

            clan.getMembers().remove(player.getUniqueId());
            plugin.getClanManager().saveClan(clan);
            return;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("accept")) {
            if (plugin.getClanManager().isInClan(player.getUniqueId())) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu befindest dich bereits in einem Clan.");
                return;
            }

            Clan invitedClan = this.clanInvite.getIfPresent(player);

            if(invitedClan == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu wurdest in keinen Clan eingeladen.");
                return;
            }

            if(invitedClan.getMembers().size() > invitedClan.getMaxMembers()) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDieser Clan ist voll.");
                return;
            }

            invitedClan.getMembers().add(player.getUniqueId());
            plugin.getClanManager().saveClan(invitedClan);
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Du bist nun ein Mitglied des Clans §e" + invitedClan.getName());

            for (UUID member : invitedClan.getMembers()) {
                Player clanPlayer = Bukkit.getPlayer(member);
                if (clanPlayer != null) {
                    clanPlayer.sendMessage(SkyMythPlugin.PREFIX + "§e" + player.getName() + " §7ist deinem Clan §abeigetreten.");
                }
            }
            Bukkit.getPlayer(invitedClan.getLeader()).sendMessage(SkyMythPlugin.PREFIX + "§e" + player.getName() + " §7ist deinem Clan §abeigetreten.");
            return;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            Clan clan = plugin.getClanManager().getClan(player.getUniqueId());

            if (clan == null) {
                player.sendMessage(SkyMythPlugin.PREFIX + "§cDu befindest dich in keinem Clan.");
                return;
            }

            player.sendMessage("§8§m------------------------------------------------------§r");
            player.sendMessage("§r");
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Dein aktueller Clan: §e" + clan.getName());
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Clanbesitzer: §e" + Bukkit.getOfflinePlayer(clan.getLeader()).getName());
            player.sendMessage(SkyMythPlugin.PREFIX + "§7Clanmitglieder: §a" + clan.getMembers().size() + " §8/ §c" + clan.getMaxMembers());
            if (!clan.getMembers().isEmpty()) {
                int i = 1;
                for (UUID member : clan.getMembers()) {
                    player.sendMessage(SkyMythPlugin.PREFIX + "§7Mitglied #" + i + ": §e" + Bukkit.getOfflinePlayer(member).getName());
                    i++;
                }
            }
            player.sendMessage("§r");
            player.sendMessage("§8§m------------------------------------------------------§r");

            return;
        }

        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /clan create <name>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /clan invite <spieler>");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /clan accept");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /clan delete");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Verwende: /clan info");


    }
}
