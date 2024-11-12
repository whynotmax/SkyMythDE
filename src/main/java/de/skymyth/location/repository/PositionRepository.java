package de.skymyth.location.repository;

import de.skymyth.location.model.Position;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

import java.util.List;

@Collection("positions")
public interface PositionRepository extends Repository<Position, String> {

    List<Position> findManyByWarp(boolean warp);

}
