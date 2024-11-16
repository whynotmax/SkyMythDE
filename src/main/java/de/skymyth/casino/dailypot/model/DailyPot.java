package de.skymyth.casino.dailypot.model;

import eu.koboo.en2do.repository.entity.Id;
import eu.koboo.en2do.repository.entity.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DailyPot {

    @Id
    String id; //NOT USED

    long pot;
    long lastPot;

    List<UUID> participants;

    UUID lastWinner;
    long lastWinnerPot;

    @Transient
    public double calculateWinChance() {
        if (participants == null || participants.isEmpty()) {
            return 100.0;
        }
        return 100.0 / participants.size();
    }

}
