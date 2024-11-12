package de.skymyth.user;

import de.skymyth.SkyMythPlugin;
import de.skymyth.user.model.User;
import de.skymyth.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserManager {

    SkyMythPlugin plugin;
    Map<UUID, User> userMap;
    UserRepository repository;

    public UserManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.userMap = new HashMap<>();
        this.repository = plugin.getMongoManager().create(UserRepository.class);
    }

    public void loadUser(UUID uuid) {
        User user = this.repository.findFirstById(uuid);

        if(user == null) {
            user = new User();
            user.setUniqueId(uuid);
            user.setBalance(750);
            user.setKills(0);
            user.setDeaths(0);
        }

        this.userMap.put(uuid, user);
    }

    public void saveUser(UUID uuid) {
        User user = this.userMap.get(uuid);
        this.repository.save(user);
    }

    public User getUser(UUID uuid) {
        return this.userMap.get(uuid);
    }


}
