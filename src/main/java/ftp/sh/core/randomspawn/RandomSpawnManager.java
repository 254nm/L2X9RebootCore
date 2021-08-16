package ftp.sh.core.randomspawn;

import ftp.sh.core.randomspawn.listeners.RespawnListener;
import ftp.sh.core.util.IOUtil;
import ftp.sh.core.L2X9RebootCore;
import ftp.sh.core.Manager;
import ftp.sh.core.util.ConfigCreator;
import ftp.sh.core.util.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;

public class RandomSpawnManager extends Manager {

    private int range;
    private String world;
    private List<Material> ignored;
    private Configuration config;

    public RandomSpawnManager() {
        super("RandomSpawn");
    }

    public String getWorld() {
        return world;
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        config = IOUtil.createConfig(plugin, getName(), getName() + "-config", "configs/random.yml").getConfig();
        plugin.registerListener(new RespawnListener(this));
        setVars();
    }

    @Override
    public void destruct(L2X9RebootCore plugin) {

    }

    @Override
    public void reloadConfig(ConfigCreator creator) {
        config = creator.getConfigs().get(getName() + "-config").getConfig();
        setVars();
    }

    private List<Material> parseIgnored() {
        List<String> strList = config.getStringList("Blocks");
        List<Material> output = new ArrayList<>();
        for (String strMat : strList) {
            try {
                Material material = Material.valueOf(strMat.toUpperCase());
                output.add(material);
            } catch (EnumConstantNotPresentException e) {
                Utils.log("&3Unknown block&r&a " + strMat + "&r&3 in blocks section of the config", this);
            }
        }
        return output;
    }

    private void setVars() {
        range = config.getInt("Range");
        world = config.getString("World");
        ignored = parseIgnored();
    }

    public List<Material> getIgnored() {
        return ignored;
    }

    public int getRange() {
        return range;
    }
}