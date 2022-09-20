package me.l2x9.core.home;

import lombok.Getter;
import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import me.l2x9.core.home.commands.DelHomeCommand;
import me.l2x9.core.home.commands.HomeCommand;
import me.l2x9.core.home.commands.SetHomeCommand;
import me.l2x9.core.home.listeners.JoinListener;
import me.l2x9.core.home.util.HomeIO;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class HomeManager extends Manager {
    private HashMap<UUID, ArrayList<Home>> homes;
    private ConfigurationSection config;
    private HomeIO homeIO;

    public HomeManager() {
        super("Homes");
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        File homesFolder = new File(getDataFolder(), "PlayerHomes");
        if (!homesFolder.exists()) homesFolder.mkdir();
        config = plugin.getModuleConfig(this);
        homeIO = new HomeIO(homesFolder);
        homes = homeIO.getHomes();
        if (!Bukkit.getOnlinePlayers().isEmpty()) Bukkit.getOnlinePlayers().forEach(homeIO::loadHomes);
        plugin.registerListener(new JoinListener(this));
        plugin.registerCommand("home", new HomeCommand(this));
        plugin.registerCommand("sethome", new SetHomeCommand(this));
        plugin.registerCommand("delhome", new DelHomeCommand(this));
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            List<Home> homes = getHomes().getOrDefault(player.getUniqueId(), null);
            if (homes == null) return Collections.emptyList();
            if (args.length < 1) {
                return homes.stream().map(Home::getName).sorted(String::compareToIgnoreCase).collect(Collectors.toList());
            } else {
                return homes.stream().map(Home::getName).filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).sorted(String::compareToIgnoreCase).collect(Collectors.toList());
            }
        } else return Collections.emptyList();
    }

    @Override
    public void destruct(L2X9RebootCore plugin) {

    }

    @Override
    public void reloadConfig(ConfigurationSection config) {
        this.config = config;
    }
}
