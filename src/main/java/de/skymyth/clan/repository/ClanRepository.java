package de.skymyth.clan.repository;

import de.skymyth.clan.model.Clan;
import eu.koboo.en2do.repository.Repository;

import java.util.UUID;

public interface ClanRepository extends Repository<Clan, String> {

    Clan findFirstByLeader(UUID leader);

}
