package net.southhollow.claims.storage;

import net.southhollow.claims.ClaimExtension;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DataStorage extends YamlConfiguration {

    private static DataStorage data;
    private final File file;
    ClaimExtension plugin = ClaimExtension.getInstance();

    public static DataStorage getConfig() {
        if (data == null) {
            data = new DataStorage();
        }
        return data;
    }

    public DataStorage() {
        file = new File(plugin.getDataFolder(), "data.dat");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        reload();
    }

    public void reload() {
        try {
            super.load(file);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            super.save(file);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}

