package de.skymyth.clan.model;

import eu.koboo.en2do.repository.entity.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Clan {

    @Id
    String name;
    UUID leader;

    long maxMembers;

    List<UUID> members;

    Location baseLocation;

}
