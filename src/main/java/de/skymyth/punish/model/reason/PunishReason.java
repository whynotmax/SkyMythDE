package de.skymyth.punish.model.reason;

import de.skymyth.punish.model.type.PunishType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Duration;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum PunishReason {

    HACKING("Hacking", "Hacking", PunishType.BAN, Duration.ofDays(30)),
    ADVERTISING("Werbung", "Werbung", PunishType.MUTE, Duration.ofDays(7)),
    SPAMMING("Spamming", "Spamming", PunishType.MUTE, Duration.ofDays(1)),
    GRIEFING("Griefing", "Griefing", PunishType.BAN, Duration.ofDays(7));

    String name;
    String description;
    PunishType type;
    Duration punishDuration;

}
