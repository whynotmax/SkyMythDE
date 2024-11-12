package de.skymyth.tpa.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Getter
@Setter
public class TeleportRequest {

    UUID from;
    UUID to;

    boolean here;
    long sent;

    public boolean isExpired() {
        return System.currentTimeMillis() - sent > 30000;
    }

    public boolean isSender(UUID uuid) {
        return from.equals(uuid);
    }

}
