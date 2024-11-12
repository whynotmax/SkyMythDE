package de.skymyth.crate.repository;

import de.skymyth.crate.model.Crate;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

@Collection("crates")
public interface CrateRepository extends Repository<Crate, String> {
}
