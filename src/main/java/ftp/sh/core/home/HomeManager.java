package ftp.sh.core.home;

import ftp.sh.core.util.IOUtil;
import ftp.sh.core.L2X9RebootCore;
import ftp.sh.core.Manager;
import ftp.sh.core.home.commands.DelHomeCommand;
import ftp.sh.core.home.commands.HomeCommand;
import ftp.sh.core.home.commands.SetHomeCommand;
import ftp.sh.core.home.listeners.JoinListener;
import ftp.sh.core.home.util.HomeUtil;
import ftp.sh.core.util.ConfigCreator;
import ftp.sh.core.util.ConfigCreator.ConfigurationWrapper;
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
        ConfigurationWrapper wrapper = IOUtil.createConfig(plugin, getName(), getName() + "-config", "configs/home.yml");
        config = wrapper.getConfig();
        File homesFolder = new File(wrapper.getDataFolder(), "PlayerHomes");
        if (!homesFolder.exists()) homesFolder.mkdir();
        homeUtil = new HomeUtil(homesFolder);
        homes = homeUtil.getHomes();
        if (!Bukkit.getOnlinePlayers().isEmpty()) Bukkit.getOnlinePlayers().forEach(p -> homeUtil.loadHomes(p));
        plugin.registerListener(new JoinListener(this));
        plugin.getCommand("home").setExecutor(new HomeCommand(this));
        plugin.getCommand("sethome").setExecutor(new SetHomeCommand(this));
        plugin.getCommand("delhome").setExecutor(new DelHomeCommand(this));
    }

    @Override
    public void destruct(L2X9RebootCore plugin) {

    }

    @Override
    public void reloadConfig(ConfigCreator creator) {
        config = creator.getConfigs().get(getName() + "-config").getConfig();
    }
}
