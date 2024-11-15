package de.skymyth.badge.repository;

import de.skymyth.badge.model.Badge;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

@Collection("badges")
public interface BadgeRepository extends Repository<Badge, String> {
}
