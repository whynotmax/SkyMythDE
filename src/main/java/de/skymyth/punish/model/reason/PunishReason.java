package de.skymyth.punish.model.reason;

import de.skymyth.punish.model.type.PunishType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.util.List;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum PunishReason {

    HACKING("Hacking", "Hacking", PunishType.BAN, Duration.ofDays(30)),
    ADVERTISING("Werbung", "Werbung", PunishType.MUTE, Duration.ofDays(7)),
    SPAMMING("Spamming", "Spamming", PunishType.MUTE, Duration.ofDays(1)),
    GRIEFING("Griefing", "Griefing", PunishType.BAN, Duration.ofDays(7)),
    PERMANENT("Permanent", "Permanent", PunishType.BAN, Duration.ofDays(365));

    String name;
    String description;
    PunishType type;
    Duration punishDuration;

    public static final PunishReason[] VALUES = values();

    public static List<PunishReason> getReasonsByType(PunishType type) {
        List<PunishReason> reasons = new java.util.ArrayList<>();
        for (PunishReason reason : VALUES) {
            if (reason.getType() == type) {
                reasons.add(reason);
            }
        }
        return reasons;
    }

}
