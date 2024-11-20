package de.skymyth.baseprotector.model;

import de.skymyth.baseprotector.model.radius.BaseProtectorRadius;
import eu.koboo.en2do.repository.entity.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BaseProtector {

    @Id
    UUID uniqueId;

    Location baseProtectorLocation;
    UUID baseOwner;
    List<UUID> trustedPlayers;

    long maxTrustedPlayers;

    BaseProtectorRadius baseProtectorRadius;


    public void addTrustedPlayer(UUID uuid) {
        this.trustedPlayers.add(uuid);
    }
}
