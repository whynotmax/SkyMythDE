package de.skymyth.clan.model;

import de.skymyth.clan.model.bank.ClanBank;
import eu.koboo.en2do.repository.entity.Id;
import eu.koboo.en2do.repository.entity.Transient;
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

    ClanBank bank;

    @Transient
    public boolean isLeader(UUID uuid) {
        return leader.equals(uuid);
    }

}
