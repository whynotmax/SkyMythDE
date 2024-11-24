package de.skymyth.maintenance.repository;

import de.skymyth.maintenance.model.Maintenance;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

@Collection("maintenance")
public interface MaintenanceRepository extends Repository<Maintenance, String> {
}
