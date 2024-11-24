package de.skymyth.badge;

import de.skymyth.SkyMythPlugin;
import de.skymyth.badge.model.Badge;
import de.skymyth.badge.repository.BadgeRepository;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BadgeManager {

    SkyMythPlugin plugin;
    BadgeRepository repository;

    Map<String, Badge> cachedBadges;

    public BadgeManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.repository = plugin.getMongoManager().create(BadgeRepository.class);

        this.cachedBadges = repository.findAll().stream().collect(Collectors.toMap(Badge::getName, badge -> badge));
    }

    public void createBadge(Badge badge) {
        if (cachedBadges.values().stream().anyMatch(b -> b.getName().equalsIgnoreCase(badge.getName()))) {
            throw new IllegalArgumentException("Badge with name " + badge.getName() + " already exists.");
        }
        repository.save(badge);
        cachedBadges.put(badge.getName(), badge);
    }

    public void deleteBadge(Badge badge) {
        if (!cachedBadges.containsValue(badge)) {
            throw new IllegalArgumentException("Badge with name " + badge.getName() + " does not exist.");
        }
        repository.delete(badge);
        cachedBadges.remove(badge.getName());
    }

    public void saveBadge(Badge badge) {
        cachedBadges.put(badge.getName(), badge);
        repository.save(badge);
    }

    public Badge getBadge(String name) {
        return cachedBadges.values().stream().filter(badge -> badge.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<Badge> getBadges(UUID user) {
        return cachedBadges.values().stream().filter(badge -> badge.getOwners().contains(user)).toList();
    }

    public List<Badge> getBadges() {
        return new ArrayList<>(cachedBadges.values());
    }

    public Badge getBadgeByCharacter(String character) {
        return cachedBadges.values().stream().filter(badge -> badge.getCharacter().equalsIgnoreCase(character)).findFirst().orElse(null);
    }
}
