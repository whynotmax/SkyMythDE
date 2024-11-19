package de.skymyth.badge.model;

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
public class Badge {

    @Id
    String name;
    String description;
    String color;

    String character;

    long creationDate;
    UUID creator;

    List<UUID> owners;

}
