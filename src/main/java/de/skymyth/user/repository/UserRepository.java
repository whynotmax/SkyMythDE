package de.skymyth.user.repository;

import de.skymyth.user.model.User;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

import java.util.UUID;

@Collection("users")
public interface UserRepository extends Repository<User, UUID> {
}
