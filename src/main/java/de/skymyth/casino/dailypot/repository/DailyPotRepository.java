package de.skymyth.casino.dailypot.repository;

import de.skymyth.casino.dailypot.model.DailyPot;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

@Collection("dailypot")
public interface DailyPotRepository extends Repository<DailyPot, String> {
}
