package ftp.sh.core.util;

import ftp.sh.core.L2X9RebootCore;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.MissingResourceException;

public class ConfigCreator {
    private final HashMap<String, ConfigurationWrapper> configs = new HashMap<>();
    private final File dataFolder;

    public ConfigCreator(String pluginName) {
        dataFolder = new File("plugins/" + pluginName);
        if (!dataFolder.exists()) dataFolder.mkdir();
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public void makeConfig(File folder, String resource, String name) {
        try {
            File dir = (folder == null) ? dataFolder : new File(dataFolder, folder.getName());
            String first = name.split("\\.")[0];
            if (!dir.exists()) dir.mkdir();
            File configFile = new File(dir, first + ".yml");
            if (!configFile.exists()) {
                InputStream stream = getClass().getClassLoader().getResourceAsStream(resource);
                if (stream == null)
                    throw new MissingResourceException("Could not find resource", getClass().getName(), first);
                Files.copy(stream, configFile.toPath());
                stream.close();
            }
            ConfigurationWrapper wrapper = new ConfigurationWrapper(configFile, YamlConfiguration.loadConfiguration(configFile), resource, dir);
            if (configs.containsKey(first)) {
                configs.replace(first, wrapper);
            } else {
                configs.put(first, wrapper);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void reloadAllConfigs() {
        configs.forEach((name, config) -> {
            File dir = (config.dataFolder == null) ? dataFolder : new File(dataFolder, config.dataFolder.getName());
            String resource = config.resourceName;
            makeConfig(dir, resource, name);
        });
        L2X9RebootCore.getInstance().getManagers().forEach(m -> m.reloadConfig(this));
    }

    public HashMap<String, ConfigurationWrapper> getConfigs() {
        return configs;
    }

    public static class ConfigurationWrapper {
        protected final String resourceName;
        protected final File dataFolder;
        private final File dataFile;
        private final Configuration config;

        public ConfigurationWrapper(File dataFile, Configuration config, String resourceName, File dataFolder) {
            this.dataFile = dataFile;
            this.config = config;
            this.resourceName = resourceName;
            this.dataFolder = dataFolder;
        }

        public File getDataFolder() {
            return dataFolder;
        }

        public File getDataFile() {
            return dataFile;
        }

        public Configuration getConfig() {
            return config;
        }
    }
}
