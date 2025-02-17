package de.skymyth.user;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;
import de.skymyth.user.model.setting.Setting;
import de.skymyth.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserManager {

    Map<UUID, User> userMap;
    UserRepository repository;

    public UserManager(SkyMythPlugin plugin) {
        this.userMap = new HashMap<>();
        this.repository = plugin.getMongoManager().create(UserRepository.class);
    }

    public void loadUser(UUID uuid) {
        User user = this.repository.findFirstById(uuid);

        if (user == null) {
            user = new User();
            user.setUniqueId(uuid);
            user.setBalance(750);
            user.setKills(0);
            user.setTrophies(0);
            user.setDeaths(0);
            user.setCooldowns(new ArrayList<>());
            user.setPerks(new HashMap<>());
            user.setJoinMessage(null);
            user.setQuitMessage(null);
            user.setSelectedBadge(null);
            user.setAdventDayOpened(new HashMap<>());
            user.setHomes(new ArrayList<>());
            user.setDiscordId(0);
            user.setSettings(new HashMap<>(Setting.DEFAULT_SETTINGS));
        }

        this.userMap.put(uuid, user);
    }

    public void saveUser(UUID uuid) {
        User user = this.userMap.get(uuid);
        this.userMap.put(uuid, user);
        this.repository.save(user);
    }

    public void saveUser(User user) {
        this.userMap.put(user.getUniqueId(), user);
        this.repository.save(user);
    }

    public void saveUsersOnlyToDatabase(List<User> users) {
        this.repository.saveAll(users);
    }

    public User getUser(UUID uuid) {
        User user = this.userMap.get(uuid);
        if (user == null) {
            this.loadUser(uuid);
            user = this.userMap.get(uuid);
        }
        return user;
    }

    public List<User> getAllUsers() {
        return List.copyOf(this.repository.findAll());
    }


}
