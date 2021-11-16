package me.l2x9.core;

import org.bukkit.configuration.file.FileConfiguration;

public interface ConfigManager {
    L2X9RebootCore plugin = L2X9RebootCore.getPlugin();
    FileConfiguration config = plugin.getConfig();
}
