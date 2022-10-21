package me.l2x9.core.randomspawn;

import me.l2x9.core.randomspawn.listeners.RespawnListener;
import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import me.l2x9.core.util.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class RandomSpawnManager extends Manager {

    private int range;
    private String world;
    private List<Material> ignored;
    private ConfigurationSection config;

    public RandomSpawnManager() {
        super("RandomSpawn");
    }

    public String getWorld() {
        return world;
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        config = plugin.getModuleConfig(this);
        plugin.registerListener(new RespawnListener(this));
        range = config.getInt("Range");
        world = config.getString("World");
        ignored = parseIgnored();
    }

    @Override
    public void destruct(L2X9RebootCore plugin) {

    }

    @Override
    public void reloadConfig(ConfigurationSection config) {
        this.config = config;
        range = config.getInt("Range");
        world = config.getString("World");
        ignored = parseIgnored();
    }

    private List<Material> parseIgnored() {
        List<String> strList = config.getStringList("Blocks");
        List<Material> output = new ArrayList<>();
        for (String strMat : strList) {
            try {
                Material material = Material.valueOf(strMat.toUpperCase());
                output.add(material);
            } catch (EnumConstantNotPresentException e) {
                Utils.log("&3Unknown block&r&a %s&r&3 in blocks section of the config", strMat);
            }
        }
        return output;
    }

    public List<Material> getIgnored() {
        return ignored;
    }

    public int getRange() {
        return range;
    }
}