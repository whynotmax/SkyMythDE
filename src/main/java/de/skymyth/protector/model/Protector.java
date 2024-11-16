package de.skymyth.protector.model;

import de.skymyth.protector.model.chunk.BaseChunk;
import eu.koboo.en2do.repository.entity.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Protector {

    @Id
    UUID owner;

    List<BaseChunk> protectedChunks;
    List<UUID> trustedPlayers;


}
