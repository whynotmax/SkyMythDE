package de.skymyth.punish.repository;

import de.skymyth.punish.model.Punish;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

@Collection("punish")
public interface PunishRepository extends Repository<Punish, String> {
}
