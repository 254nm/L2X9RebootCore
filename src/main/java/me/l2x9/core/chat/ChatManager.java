package me.l2x9.core.chat;

import me.l2x9.core.chat.commands.IgnoreCommand;
import me.l2x9.core.chat.commands.MessageCommand;
import me.l2x9.core.chat.commands.ReplyCommand;
import me.l2x9.core.chat.commands.ToggleChatCommand;
import me.l2x9.core.chat.listeners.AntiLink;
import me.l2x9.core.chat.listeners.ChatListener;
import me.l2x9.core.chat.listeners.JoinLeaveListener;
import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import me.l2x9.core.util.ConfigCreator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class ChatManager extends Manager {
    private final HashMap<UUID, ChatInfo> map;
    private ConfigCreator.ConfigurationWrapper config;
    private File ignoresFolder;

    public ChatManager() {
        super("ChatControl");
        map = new HashMap<>();
    }

    public ConfigCreator.ConfigurationWrapper getConfig() {
        return config;
    }

    public File getIgnoresFolder() {
        return ignoresFolder;
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        File dataFolder = new File(plugin.getCreator().getDataFolder(), "ChatControl");
        if (!dataFolder.exists()) dataFolder.mkdir();
        plugin.getCreator().makeConfig(dataFolder, "configs/chat-config.yml", "ChatConfig.yml");
        config = plugin.getCreator().getConfigs().get("ChatConfig");
        ignoresFolder = new File(dataFolder, "IgnoreLists");
        if (!ignoresFolder.exists()) ignoresFolder.mkdir();
        plugin.registerListener(new ChatListener(this));
        plugin.registerListener(new JoinLeaveListener(this));
        plugin.registerListener(new AntiLink(this));
        plugin.getCommand("ignore").setExecutor(new IgnoreCommand(this));
        plugin.getCommand("msg").setExecutor(new MessageCommand(this));
        plugin.getCommand("reply").setExecutor(new ReplyCommand(this));
        plugin.getCommand("togglechat").setExecutor(new ToggleChatCommand(this));
        if (!Bukkit.getOnlinePlayers().isEmpty()) Bukkit.getOnlinePlayers().forEach(this::registerPlayer);

    }

    @Override
    public void destruct(L2X9RebootCore plugin) {

    }

    @Override
    public void reloadConfig(ConfigCreator creator) {
        config = creator.getConfigs().get("ChatConfig");
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
