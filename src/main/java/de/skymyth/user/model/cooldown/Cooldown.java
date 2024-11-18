package de.skymyth.user.model.cooldown;

import eu.koboo.en2do.repository.entity.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Duration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Cooldown {

    String name;
    Duration duration;
    long start;

    @Transient
    public boolean isExpired() {
        return getRemainingTime() <= 0;
    }

    @Transient
    public void start() {
        this.start = System.currentTimeMillis();
    }

    @Transient
    public long getRemainingTime() {
        return (start + duration.toMillis()) - System.currentTimeMillis();
    }

}
