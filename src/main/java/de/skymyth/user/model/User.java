package de.skymyth.user.model;

import eu.koboo.en2do.repository.entity.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    long playTime;
    long lastSeen;

    public void addBalance(long amount) {
        this.balance += amount;
    }

    public void removeBalance(long amount) {
        this.balance -= amount;
    }

    public void addKill() {
        this.kills++;
    }

    public void addDeath() {
        this.deaths++;
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

}
