package de.skymyth.user.model;

import eu.koboo.en2do.repository.entity.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    UUID uniqueId;

    long balance;
    long kills;
    long deaths;
    long trophies;


}
