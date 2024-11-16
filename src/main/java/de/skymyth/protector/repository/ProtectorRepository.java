package de.skymyth.protector.repository;

import de.skymyth.protector.model.Protector;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

import java.util.UUID;

@Collection("protectors")
public interface ProtectorRepository extends Repository<Protector, UUID> {
}
