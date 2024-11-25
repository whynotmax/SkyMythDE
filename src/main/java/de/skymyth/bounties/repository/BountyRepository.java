package de.skymyth.bounties.repository;

import de.skymyth.bounties.model.Bounty;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

import java.util.UUID;

@Collection("bounties")
public interface BountyRepository extends Repository<Bounty, UUID> {
}
