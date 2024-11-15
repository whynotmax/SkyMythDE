package de.skymyth.punish.model;

import de.skymyth.punish.model.reason.PunishReason;
import de.skymyth.punish.model.type.PunishType;
import eu.koboo.en2do.repository.entity.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Punish {

    @Id
    String id;

    UUID target;
    PunishType type;
    PunishReason reason;

    long start;

    public long getRemaining() {
        return reason.getPunishDuration().toMillis() - (System.currentTimeMillis() - start);
    }

}
