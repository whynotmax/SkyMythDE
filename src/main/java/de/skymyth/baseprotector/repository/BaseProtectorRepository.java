package de.skymyth.baseprotector.repository;

import de.skymyth.baseprotector.model.BaseProtector;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

import java.util.UUID;

@Collection("baseprotectors")
public interface BaseProtectorRepository extends Repository<BaseProtector, UUID> {
}
