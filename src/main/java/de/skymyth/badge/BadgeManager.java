package de.skymyth.badge;

import de.skymyth.SkyMythPlugin;
import de.skymyth.badge.model.Badge;
import de.skymyth.badge.repository.BadgeRepository;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BadgeManager {

    SkyMythPlugin plugin;
    BadgeRepository badgeRepository;

    List<Badge> cachedBadges;

    public BadgeManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.badgeRepository = plugin.getMongoManager().create(BadgeRepository.class);

        this.cachedBadges = new ArrayList<>(badgeRepository.findAll());
    }

    public void createBadge(Badge badge) {
        badgeRepository.save(badge);
        cachedBadges.add(badge);
    }

    public void deleteBadge(Badge badge) {
        badgeRepository.delete(badge);
        cachedBadges.remove(badge);
    }

    public Badge getBadge(String name) {
        return cachedBadges.stream().filter(badge -> badge.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<Badge> getBadges(UUID user) {
        return cachedBadges.stream().filter(badge -> badge.getOwners().contains(user)).toList();
    }

}
