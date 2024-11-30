package de.skymyth.pvp.punish.repository;

import de.skymyth.pvp.punish.model.PvPPunishment;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

import java.util.UUID;

@Collection("pvp_punishments")
public interface PvPPunishmentRepository extends Repository<PvPPunishment, UUID> {
}
