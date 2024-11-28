package de.skymyth.bounties.model;

import eu.koboo.en2do.repository.entity.Id;
import eu.koboo.en2do.repository.entity.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Bounty {

    @Id
    UUID target;
    Map<UUID, Long> hunters;

    @Transient
    public long getReward() {
        return hunters.values().stream().mapToLong(Long::longValue).sum();
    }

}
