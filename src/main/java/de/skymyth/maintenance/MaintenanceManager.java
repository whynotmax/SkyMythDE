package de.skymyth.maintenance;

import de.skymyth.SkyMythPlugin;
import de.skymyth.maintenance.model.Maintenance;
import de.skymyth.maintenance.repository.MaintenanceRepository;
import lombok.Getter;

import java.util.UUID;

public class MaintenanceManager {

    SkyMythPlugin plugin;
    MaintenanceRepository repository;
    @Getter
    Maintenance maintenance;

    public MaintenanceManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.repository = plugin.getMongoManager().create(MaintenanceRepository.class);
        this.maintenance = this.repository.findFirstById("maintenance");
        if (this.maintenance == null) {
            this.maintenance = new Maintenance();
            this.maintenance.setId("maintenance");
            this.repository.save(this.maintenance);
        }
    }

    public void enable() {
        this.maintenance.setEnabled(true);
        this.repository.save(this.maintenance);
    }

    public void disable() {
        this.maintenance.setEnabled(false);
        this.repository.save(this.maintenance);
    }

    public void setMotdLine(int line, String motd) {
        if (line == 1) {
            this.maintenance.setMotdLine1(motd);
        } else if (line == 2) {
            this.maintenance.setMotdLine2(motd);
        }
        this.repository.save(this.maintenance);
    }

    public void addWhitelist(UUID uniqueId) {
        this.maintenance.getWhitelist().add(uniqueId);
        this.repository.save(this.maintenance);
    }

    public void removeWhitelist(UUID uniqueId) {
        this.maintenance.getWhitelist().remove(uniqueId);
        this.repository.save(this.maintenance);
    }

    public boolean isWhitelisted(UUID uniqueId) {
        return this.maintenance.getWhitelist().contains(uniqueId);
    }

}
