package de.skymyth.motd;

import de.skymyth.SkyMythPlugin;
import de.skymyth.utility.config.Config;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MOTDManager {

    SkyMythPlugin plugin;
    Config motdConfig;

    String motdLine1;
    String motdLine2;

    public MOTDManager(SkyMythPlugin plugin) {
        this.plugin = plugin;
        this.motdConfig = new Config(plugin.getDataFolder().getPath(), "motd.yml");

        this.motdLine1 = this.motdConfig.getConfiguration().getString("line1", SkyMythPlugin.PREFIX + "§c01.12.2024");
        this.motdLine2 = this.motdConfig.getConfiguration().getString("line2", "§7§oAktuell ist Wartung - bitte später wieder versuchen.");
    }

    public void setMotdLine(int line, String motd) {
        if (line == 1) {
            this.motdLine1 = motd.replace('&', '§');
        } else if (line == 2) {
            this.motdLine2 = motd.replace('&', '§');
        }
        this.save();
    }

    public void save() {
        this.motdConfig.getConfiguration().set("line1", this.motdLine1);
        this.motdConfig.getConfiguration().set("line2", this.motdLine2);
        this.motdConfig.save();
    }

}
