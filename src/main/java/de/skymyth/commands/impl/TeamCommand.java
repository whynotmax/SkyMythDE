package de.skymyth.commands.impl;

import de.skymyth.SkyMythPlugin;
import de.skymyth.commands.MythCommand;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamCommand extends MythCommand {

    public TeamCommand(SkyMythPlugin plugin) {
        super("team", null, new ArrayList<>() {{
            add("teamlist");
        }}, plugin);
    }

    @Override
    public void run(Player player, String[] args) {
        List<Group> teams = sortGroupsByWeight(getGroupsByMetaDataValue());
        Map<Group, List<User>> usersByGroup;

        if (teams.isEmpty()) {
            player.sendMessage(SkyMythPlugin.PREFIX + "§cEs wurden keine Teams gefunden.");
            return;
        }

        usersByGroup = new HashMap<>();
        LuckPermsProvider.get().getUserManager().getUniqueUsers().join().forEach(user -> {
            User luckPermsUser = LuckPermsProvider.get().getUserManager().loadUser(user).join();
            Group group = LuckPermsProvider.get().getGroupManager().getGroup(luckPermsUser.getPrimaryGroup());
            if (group == null) return;
            if (group.getCachedData().getMetaData().getMetaValue("team") != null) {
                usersByGroup.computeIfAbsent(group, k -> new ArrayList<>()).add(luckPermsUser);
            }
        });

        player.sendMessage("§8§m------------------------------------------------------§r");
        player.sendMessage("§r");
        player.sendMessage(SkyMythPlugin.PREFIX + "§7Unser Team:");
        player.sendMessage("§r");
        for (Group team : teams) {
            List<User> users = usersByGroup.get(team);
            Map<UUID, Boolean> onlineUsers = new HashMap<>();
            if (users != null) {
                users.forEach(user -> {
                    OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(user.getUniqueId());
                    onlineUsers.put(user.getUniqueId(), offlinePlayer.isOnline());
                });
            }
            if (users == null) continue;
            player.sendMessage("§r " + team.getCachedData().getMetaData().getMetaValue("chat-prefix").substring(0, team.getCachedData().getMetaData().getMetaValue("chat-prefix").length() - 7).replace('&', '§') + " §8× §a" + onlineUsers.values().stream().filter(bool -> bool).count() + " online");
            StringBuilder usersString = new StringBuilder();
            users.forEach(user -> {
                OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(user.getUniqueId());
                usersString.append(onlineUsers.get(user.getUniqueId()) ? "§a" : "§c").append(offlinePlayer.getName()).append("§7, ");
            });
            player.sendMessage("§r  §8- §7" + usersString.substring(0, usersString.length() - 4));
            player.sendMessage("§r");
        }
        player.sendMessage("§8§m------------------------------------------------------§r");
    }

    private List<Group> sortGroupsByWeight(List<Group> groups) {
        return groups.stream()
                .sorted((group1, group2) -> {
                    int weight1 = group1.getWeight().getAsInt();
                    int weight2 = group2.getWeight().getAsInt();
                    return Integer.compare(weight1, weight2);
                })
                .toList();
    }

    private List<Group> getGroupsByMetaDataValue() {
        return LuckPermsProvider.get().getGroupManager().getLoadedGroups().stream()
                .filter(group -> group.getCachedData().getMetaData().getMetaValue("team") != null)
                .toList();
    }
}
