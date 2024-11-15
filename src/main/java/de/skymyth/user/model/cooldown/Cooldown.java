package de.skymyth.user.model.cooldown;

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

    public boolean isExpired() {
        return System.currentTimeMillis() - start >= duration.toMillis();
    }

    public void start() {
        this.start = System.currentTimeMillis();
    }

    public long getRemainingTime() {
        return duration.toMillis() - (System.currentTimeMillis() - start);
    }

}
