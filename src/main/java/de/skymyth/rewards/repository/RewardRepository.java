package de.skymyth.rewards.repository;

import de.skymyth.rewards.model.Reward;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

import java.util.List;

@Collection("rewards")
public interface RewardRepository extends Repository<Reward, String> {

    List<Reward> findManyBySeason(String season);

}
