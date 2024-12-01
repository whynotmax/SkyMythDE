package de.skymyth.freesigns.repository;

import de.skymyth.freesigns.model.FreeSign;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

@Collection("freesigns")
public interface FreeSignRepository extends Repository<FreeSign, Integer> {
}
