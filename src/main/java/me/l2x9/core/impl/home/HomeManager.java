package me.l2x9.core.impl.home;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import me.l2x9.core.boiler.util.ConfigCreator;
import me.l2x9.core.boiler.util.IOUtil;
import me.l2x9.core.impl.home.commands.DelHomeCommand;
import me.l2x9.core.impl.home.commands.HomeCommand;
import me.l2x9.core.impl.home.commands.SetHomeCommand;
import me.l2x9.core.impl.home.listeners.JoinListener;
import me.l2x9.core.impl.home.util.HomeUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HomeManager extends Manager {
    private HashMap<UUID, List<Home>> homes;
    private Configuration config;
    private HomeUtil homeUtil;

    public HomeManager() {
        super("Homes");
    }

    public HomeUtil getHomeUtil() {
        return homeUtil;
    }

    public HashMap<UUID, List<Home>> getHomes() {
        return homes;
    }

    public Configuration getConfig() {
        return config;
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        ConfigCreator.ConfigurationWrapper wrapper = IOUtil.createConfig(plugin, getName(), getName() + "-config", "configs/home.yml");
        config = wrapper.getConfig();
        File homesFolder = new File(wrapper.getDataFolder(), "PlayerHomes");
        if (!homesFolder.exists()) homesFolder.mkdir();
        homeUtil = new HomeUtil(homesFolder);
        homes = homeUtil.getHomes();
        if (!Bukkit.getOnlinePlayers().isEmpty()) Bukkit.getOnlinePlayers().forEach(p -> homeUtil.loadHomes(p));
        plugin.registerListener(new JoinListener(this));
        plugin.registerCommand("home", new HomeCommand(this));
        plugin.registerCommand("sethome", new SetHomeCommand(this));
        plugin.registerCommand("delhome", new DelHomeCommand(this));
    }

    @Override
    public void destruct(L2X9RebootCore plugin) {

    }

    @Override
    public void reloadConfig(ConfigCreator creator) {
        config = creator.getConfigs().get(getName() + "-config").getConfig();
    }
}
