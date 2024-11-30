package de.skymyth.pvp.punish.model;

import eu.koboo.en2do.repository.entity.Id;
import eu.koboo.en2do.repository.entity.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PvPPunishment {

    @Id
    UUID uniqueId;

    String reason;
    long duration;

    @Transient
    public boolean isPermanent() {
        return duration == -1;
    }

    @Transient
    public Duration getRemainingTime() {
        return Duration.ofMillis((duration - System.currentTimeMillis()));
    }

    @Transient
    public boolean isExpired() {
        return duration != -1 && System.currentTimeMillis() >= duration;
    }

}

