package de.skymyth.protector.model;

import eu.koboo.en2do.repository.entity.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Protector {

    @Id
    UUID protectorUniqueId;

    UUID owner;
    Location location;

    /**
     * In Blocks
     */
    List<Chunk> chunks;

    List<UUID> trusted;
    List<UUID> denied;

}
