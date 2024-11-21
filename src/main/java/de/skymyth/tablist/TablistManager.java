package de.skymyth.tablist;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.Util;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.util.*;

public class TablistManager {

    SkyMythPlugin plugin;

    Scoreboard scoreboard;
    Map<String, Team> teamMap;

    public TablistManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
        this.teamMap = new HashMap<>();

        int teamBefore = 0;
        for (Group group : getGroupsSortedByWeight()) {
            final String name;
            if (teamBefore < 10) {
                name = "0" + teamBefore + group.getName();
            } else {
                name = teamBefore + group.getName();
            }
            teamBefore++;
            String prefix = group.getCachedData().getMetaData().getPrefix() != null ? group.getCachedData().getMetaData().getPrefix() : "";
            String suffix = group.getCachedData().getMetaData().getSuffix() != null ? group.getCachedData().getMetaData().getSuffix() : "";
            Team scoreboardTeam = scoreboard.registerNewTeam(Objects.equals(group.getName(), "default") ? "999default" : name);
            scoreboardTeam.setPrefix(prefix.replace("&", "§"));
            scoreboardTeam.setSuffix(suffix.replace("&", "§"));
            teamMap.put(group.getName(), scoreboardTeam);
        }
//        for (Map.Entry<String, Team> teamEntry : teamMap.entrySet()) {
//            System.out.println("Team: " + teamEntry.getKey() + " - " + teamEntry.getValue().getName());
//        }
    }

    public void setRank(Player player) {
        String group = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();
        Team team = teamMap.get(group);
        if (team == null) {
            team = scoreboard.getTeam("999default");
        }
        team.addPlayer(player);
        player.setDisplayName(team.getPrefix() + player.getName() + (plugin.getClanManager().isInClan(player.getUniqueId()) ? " §8[#" + (plugin.getClanManager().getClan(player.getUniqueId()).getName().equalsIgnoreCase("Team") ? Util.christmasColor(plugin.getClanManager().getClan(player.getUniqueId()).getName()) : "§e" + plugin.getClanManager().getClan(player.getUniqueId()).getName()) + "§8]" : ""));
        player.setPlayerListName(team.getPrefix() + player.getName() + (plugin.getClanManager().isInClan(player.getUniqueId()) ? " §8[#" + (plugin.getClanManager().getClan(player.getUniqueId()).getName().equalsIgnoreCase("Team") ? Util.christmasColor(plugin.getClanManager().getClan(player.getUniqueId()).getName()) : "§e" + plugin.getClanManager().getClan(player.getUniqueId()).getName()) + "§8]" : "")
                + (Util.VANISH.contains(player) ? " §8» §bV" : ""));
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            onlinePlayer.setScoreboard(scoreboard);
            updateTablistHeaderAndFooter(onlinePlayer);
        });
    }

    public void updateTablistHeaderAndFooter(Player player) {

        double tps = MinecraftServer.getServer().recentTps[0];

        sendTab(player,
                "§r\n" +
                        "§8» §5§lSkyMyth§8.§5§lDE §8┃ §cWeihnachten 2024 §4❤ §8«\n" +
                        "§r\n" +
                        "§2✥ §8» §7Aktueller Server: §aSkyPvP\n" +
                        "§9✦ §8» §7Spieler Online: §c" + (Bukkit.getOnlinePlayers().size() - Util.VANISH.size()) +
                        (player.isOp() ? "\n\n §8➥ §7Aktuelle TPS: §a" + Math.round(tps) + " §8× §7Aktuelle CPU: §a" + Util.getProcessCpuLoad() + "%%" + "\n" : "\n"),
                "\n" +
                        "§7Heute schon gevotet? §8» §c/vote \n" +
                        "§7Discord§8: §cdiscord.SkyMyth.de §8× §7Bewerben§8: §capply.skymyth.de§r" +
                        "\n");
    }

    private void sendTab(Player player, String head, String foot) {
        IChatBaseComponent header = new ChatMessage(head);
        IChatBaseComponent footer = new ChatMessage(foot);
        PacketPlayOutPlayerListHeaderFooter tabList = new PacketPlayOutPlayerListHeaderFooter();

        try {
            Field headerField = tabList.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(tabList, header);
            Field footerField = tabList.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(tabList, footer);
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        CraftPlayer cp = (CraftPlayer) player;
        cp.getHandle().playerConnection.sendPacket(tabList);
    }

    public String getRankPrefix(Player player) {
        String group = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();
        return teamMap.get(group).getPrefix();
    }

    public String getRankPrefixChat(Player player) {
        String group = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();
        return teamMap.get(group).getDisplayName();
    }

    private List<Group> getGroupsSortedByWeight() {
        List<Group> groups = new ArrayList<>(LuckPermsProvider.get().getGroupManager().getLoadedGroups());
        groups.sort(Comparator.comparingInt(i -> i != null ? i.getWeight().orElse(0) : 0));
        return groups;
    }

}