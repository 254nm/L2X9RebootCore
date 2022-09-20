package me.l2x9.core.chat;

import me.l2x9.core.chat.commands.*;
import me.l2x9.core.chat.listeners.ChatListener;
import me.l2x9.core.chat.listeners.CommandWhitelist;
import me.l2x9.core.chat.listeners.JoinLeaveListener;
import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import me.l2x9.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class ChatManager extends Manager {
    private final HashMap<UUID, ChatInfo> map;
    private ConfigurationSection config;
    private File ignoresFolder;

    public ChatManager() {
        super("ChatControl");
        map = new HashMap<>();
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    public File getIgnoresFolder() {
        return ignoresFolder;
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdir();
        File tldFile = new File(dataFolder, "tlds.txt");
        if (!tldFile.exists()) Utils.unpackResource("tlds.txt", tldFile);
        ignoresFolder = new File(dataFolder, "IgnoreLists");
        if (!ignoresFolder.exists()) ignoresFolder.mkdir();
        config = plugin.getModuleConfig(this);
        plugin.registerListener(new ChatListener(this, parseTLDS(tldFile)));
        plugin.registerListener(new JoinLeaveListener(this));
        plugin.registerListener(new CommandWhitelist(this));
        plugin.registerCommand ("ignore",new IgnoreCommand(this));
        plugin.getCommand("msg").setExecutor(new MessageCommand(this));
        plugin.getCommand("reply").setExecutor(new ReplyCommand(this));
        plugin.registerCommand("togglechat",new ToggleChatCommand(this));
        plugin.registerCommand("unignore",new UnIgnoreCommand(this));
        if (!Bukkit.getOnlinePlayers().isEmpty()) Bukkit.getOnlinePlayers().forEach(this::registerPlayer);

    }

    private HashSet<String> parseTLDS(File tldFile) {
        try {
            HashSet<String> buf = new HashSet<>();
            BufferedReader reader = new BufferedReader(new FileReader(tldFile));
            reader.lines().filter(l -> !l.startsWith("#")).forEach(s -> buf.add(s.toLowerCase()));
            reader.close();
            return buf;
        } catch (Throwable t) {
            Utils.log("&cFailed to parse the TLD file please see the stacktrace below for more info!");
            t.printStackTrace();
            return null;
        }
    }

    @Override
    public void destruct(L2X9RebootCore plugin) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            ChatInfo ci = getInfo(p);
            ci.saveIgnores();
        });
    }

    @Override
    public void reloadConfig(ConfigurationSection config) {
        this.config = config;
    }

    public void registerPlayer(Player player) {
        map.put(player.getUniqueId(), new ChatInfo(player, this));
    }

    public void removePlayer(Player player) {
        map.remove(player.getUniqueId());
    }

    public ChatInfo getInfo(Player player) {
        return map.getOrDefault(player.getUniqueId(), null);
    }
}
