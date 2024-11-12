package de.skymyth.utility;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {

    File file;
    @Getter
    YamlConfiguration configuration;

    public Config(String path, String name) {
        this.file = new File(path, name);
        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            this.file = new File(path, name);
        }
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void save() {
        try {
            this.configuration.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}