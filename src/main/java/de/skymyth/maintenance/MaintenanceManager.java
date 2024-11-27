package de.skymyth.maintenance;

import de.skymyth.SkyMythPlugin;
import de.skymyth.maintenance.model.Maintenance;
import de.skymyth.maintenance.repository.MaintenanceRepository;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
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
            this.maintenance.setMotdLine1("§7Der Server ist momentan im Wartungsmodus.");
            this.maintenance.setMotdLine2("§7Unser Release ist am §e01. Dezember 2024§7.");
            this.maintenance.setEnabled(false);
            this.maintenance.setWhitelist(new ArrayList<>());
            this.maintenance.setId("maintenance");
            this.repository.save(this.maintenance);
        }
    }

    public String getMaintenanceScreen() {
        return "\n" +
                SkyMythPlugin.PREFIX + "§7Wartungsarbeiten\n" +
                "\n" +
                "§7Der Server ist momentan im Wartungsmodus.\n" +
                "§7Unser Release ist am §e01. Dezember 2024§7.\n" +
                "§7Bitte versuche es später erneut.\n" +
                "\n" +
                "§7Mehr Informationen auf unserem §bDiscord§7:\n" +
                "§3§ndiscord.skymyth.de§r\n" +
                "\n" +
                SkyMythPlugin.PREFIX + "§7Wartungsarbeiten";
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

    public List<UUID> getWhitelist() {
        return this.maintenance.getWhitelist();
    }

    public void removeWhitelist(UUID uniqueId) {
        this.maintenance.getWhitelist().remove(uniqueId);
        this.repository.save(this.maintenance);
    }

    public boolean isWhitelisted(UUID uniqueId) {
        return this.maintenance.getWhitelist().contains(uniqueId);
    }

}
