package de.skymyth.user.model;

import de.skymyth.advent.model.AdventDay;
import de.skymyth.perks.model.Perks;
import de.skymyth.user.model.cooldown.Cooldown;
import eu.koboo.en2do.repository.entity.Id;
import eu.koboo.en2do.repository.entity.Transient;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    UUID uniqueId;

    long balance;
    long kills;
    long deaths;
    long trophies;
    @Transient
    long pvpShards;

    long playTime;
    long lastSeen;

    List<Cooldown> cooldowns;
    Map<Perks, Long> perks;
    Map<Integer, Boolean> adventDayOpened;

    String selectedBadge;

    String joinMessage;
    String quitMessage;

    public void addBalance(long amount) {
        this.balance += amount;
    }

    public void openAdvent(int day) {
        this.adventDayOpened.put(day, true);
    }


    public void removeBalance(long amount) {
        this.balance -= amount;
    }

    public void removePvPShards(long amount) {
        this.pvpShards -= amount;
    }

    public void addKill() {
        this.kills++;
    }

    public void addDeath() {
        this.deaths++;
    }

    public void addPvPShards(long amount) {
        this.pvpShards += amount;
    }

    public void addTrophy() {
        this.trophies++;
    }

    public void addTrophies(long amount) {
        this.trophies += amount;
    }

    public void removeTrophies(long amount) {
        this.trophies -= amount;
    }

    public void updatePlayTime() {
        this.playTime += System.currentTimeMillis() - this.lastSeen;
        this.lastSeen = System.currentTimeMillis();
    }

    public String getKillDeathRatio() {
        return this.deaths == 0 ? "∞" : (this.kills == 0 ? "0.00" : String.format("%.2f", (double) this.kills / this.deaths));
    }

    public Cooldown getCooldown(String name) {
        return this.cooldowns.stream().filter(cooldown -> cooldown.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void addCooldown(Cooldown cooldown) {
        this.cooldowns.add(cooldown);
    }

    public void removeCooldown(String name) {
        this.cooldowns.removeIf(cooldown -> cooldown.getName().equalsIgnoreCase(name));
    }

    public boolean hasCooldown(String name) {
        return this.cooldowns.stream().anyMatch(cooldown -> cooldown.getName().equalsIgnoreCase(name));
    }

    public boolean isOnCooldown(String name) {
        Cooldown cooldown = this.getCooldown(name);
        return cooldown != null && !cooldown.isExpired();
    }

    public boolean hasJoinMessage() {
        return this.joinMessage != null && !this.joinMessage.isEmpty();
    }

    public boolean hasQuitMessage() {
        return this.quitMessage != null && !this.quitMessage.isEmpty();
    }

    public void addPerk(Perks perk, long duration) {
        this.perks.put(perk, System.currentTimeMillis() + duration);
    }

    public void removePerk(Perks perk) {
        this.perks.remove(perk);
    }

    public boolean hasPerk(Perks perk) {
        return this.perks.containsKey(perk);
    }

    public boolean isPerkActive(Perks perk) {
        return this.perks.containsKey(perk) && this.perks.get(perk) > System.currentTimeMillis();
    }

    public long getPerkDuration(Perks perk) {
        return this.perks.get(perk) - System.currentTimeMillis();
    }

}
