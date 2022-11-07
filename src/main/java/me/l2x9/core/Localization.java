package me.l2x9.core;

import lombok.RequiredArgsConstructor;
import me.l2x9.core.util.Utils;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

@RequiredArgsConstructor
public class Localization {
    private static HashMap<String, Localization> localizationMap;
    private final Configuration config;

    protected static void loadLocalizations(File dataFolder) {
        if (localizationMap != null) localizationMap.clear();
        localizationMap = new HashMap<>();
        File localeDir = new File(dataFolder, "Localization");
        if (!localeDir.exists()) localeDir.mkdirs();
        Utils.unpackResource("localization/en_us.yml", new File(localeDir, "en_us.yml"));
        for (File ymlFile : localeDir.listFiles(f -> f.getName().endsWith(".yml"))) {
            Configuration config = YamlConfiguration.loadConfiguration(ymlFile);
            localizationMap.put(ymlFile.getName().replace(".yml", ""), new Localization(config));
        }

    }

    public static Localization getLocalization(String locale) {
        return localizationMap.getOrDefault(locale, localizationMap.get("en_us"));
    }

    public String get(String key) {
        return config.getString(key, String.format("Unknown key %s", key));
    }
}