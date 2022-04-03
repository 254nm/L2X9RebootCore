package me.l2x9.core.impl.home;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import me.l2x9.core.impl.home.commands.DelHomeCommand;
import me.l2x9.core.impl.home.commands.HomeCommand;
import me.l2x9.core.impl.home.commands.SetHomeCommand;
import me.l2x9.core.impl.home.listeners.JoinListener;
import me.l2x9.core.impl.home.util.HomeUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class HomeManager extends Manager {
    private HashMap<UUID, ArrayList<Home>> homes;
    private ConfigurationSection config;
    private HomeUtil homeUtil;

    public HomeManager() {
        super("Homes");
    }

    public HomeUtil getHomeUtil() {
        return homeUtil;
    }

    public HashMap<UUID, ArrayList<Home>> getHomes() {
        return homes;
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        File homesFolder = new File(getDataFolder(), "PlayerHomes");
        if (!homesFolder.exists()) homesFolder.mkdir();
        config = plugin.getModuleConfig(this);
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
    public void reloadConfig(ConfigurationSection config) {
        this.config = config;
    }
}
