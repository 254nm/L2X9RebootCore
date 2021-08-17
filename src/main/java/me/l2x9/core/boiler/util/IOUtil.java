package me.l2x9.core.boiler.util;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.boiler.util.ConfigCreator.ConfigurationWrapper;


import java.io.File;

public class IOUtil {
    public static ConfigurationWrapper createConfig(L2X9RebootCore plugin, String dirName, String fileName, String resource) {
        File dataFolder = new File(plugin.getCreator().getDataFolder(), dirName);
        if (!dataFolder.exists()) dataFolder.mkdir();
        plugin.getCreator().makeConfig(dataFolder, resource, fileName);
       return plugin.getCreator().getConfigs().get(fileName);
    }
}
