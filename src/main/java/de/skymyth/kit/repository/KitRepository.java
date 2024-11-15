package de.skymyth.kit.repository;

import de.skymyth.kit.model.Kit;
import de.skymyth.kit.model.type.KitType;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

import java.util.List;

@Collection("kits")
public interface KitRepository extends Repository<Kit, String> {

    List<Kit> findManyByType(KitType type);

}
