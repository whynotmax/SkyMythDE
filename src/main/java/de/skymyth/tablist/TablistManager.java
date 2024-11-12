package de.skymyth.tablist;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class TablistManager {

    Map<String, Team> teams = new HashMap<>();
    Map<Group, String> groups = new HashMap<>();
    Scoreboard scoreboard;

    public TablistManager() {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        String beforeTeam = "00";

        for (Group group : LuckPermsProvider.get().getGroupManager().getLoadedGroups()) {
            if (group.getName().equalsIgnoreCase("default")) {
                beforeTeam = "99";
                continue;
            }
            Team team = this.scoreboard.registerNewTeam(beforeTeam + group.getName());
            team.setPrefix(group.getCachedData().getMetaData().getPrefix().replace('&', '§') + "§7");

            this.teams.put(beforeTeam + group.getName(), team);
            this.groups.put(group, beforeTeam + group.getName());

            int before = Integer.parseInt(beforeTeam);
            before++;
            if (before >= 100) {
                before = 0;
            }
            if (before < 10) {
                beforeTeam = "0" + before;
            } else {
                beforeTeam = String.valueOf(before);
            }
        }
    }

    public void setPrefix(Player player) {
        Group playerGroup = LuckPermsProvider.get().getGroupManager().getGroup(LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getPrimaryGroup());
        Team team = this.teams.get(this.groups.get(playerGroup));
        if (team != null) {
            team.addPlayer(player);
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                onlinePlayer.setScoreboard(this.scoreboard);
            });
        }
    }

    public void setPrefixForAll() {
        Bukkit.getOnlinePlayers().forEach(this::setPrefix);
    }

}
