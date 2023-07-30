package me.l2x9.core;

import lombok.Getter;
import me.l2x9.core.chat.ChatManager;
import me.l2x9.core.command.CommandManager;
import me.l2x9.core.home.HomeManager;
import me.l2x9.core.misc.MiscManager;
import me.l2x9.core.patches.PatchManager;
import me.l2x9.core.patches.listeners.LightLag;
import me.l2x9.core.randomspawn.RandomSpawnManager;
import me.l2x9.core.tablist.TabManager;
import me.txmc.protocolapi.PacketEventDispatcher;
import me.txmc.protocolapi.PacketListener;
import me.txmc.protocolapi.reflection.ClassProcessor;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class L2X9RebootCore extends JavaPlugin {
    //TODO: Migrate all file storage to mysql
    @Getter private static L2X9RebootCore instance;
    @Getter private final long startTime = System.currentTimeMillis();
    private PacketEventDispatcher dispatcher;
    @Getter private ScheduledExecutorService executorService;
    private List<ViolationManager> violationManagers;
    @Getter private List<Manager> managers;

    public boolean isDebug() {
        if (System.getProperty("l2x9coredebug") == null) return false;
        return Boolean.parseBoolean(System.getProperty("l2x9coredebug"));
    }

    @Override
    public void onLoad() {
        new LightLag();
    }

    @Override
    public void onEnable() {
        instance = this;
        Localization.loadLocalizations(getDataFolder());
        dispatcher = new PacketEventDispatcher(this);
        managers = new ArrayList<>();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getLogger().addHandler(new LoggerHandler());
        saveDefaultConfig();
        violationManagers = new ArrayList<>();
        executorService = Executors.newScheduledThreadPool(4);
        executorService.scheduleAtFixedRate(() -> violationManagers.forEach(ViolationManager::decrementAll), 0, 1, TimeUnit.SECONDS);
        registerManagers();
    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
        managers.forEach(m -> m.destruct(this));
        managers.clear();
        violationManagers.clear();
        executorService.shutdown();
    }

    private void registerManagers() {
        registerManager(new TabManager());
        registerManager(new HomeManager());
        registerManager(new RandomSpawnManager());
        registerManager(new CommandManager());
        registerManager(new MiscManager());
        registerManager(new ChatManager());
        registerManager(new PatchManager());
        managers.forEach(manager -> manager.init(this));
    }


    public void registerViolationManager(ViolationManager manager) {
        if (violationManagers.contains(manager)) return;
        violationManagers.add(manager);
    }

    private void registerManager(Manager manager) {
        managers.add(manager);
    }


    public void registerListener(Listener listener) {
        if (ClassProcessor.hasAnnotation(listener)) ClassProcessor.process(listener);
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @SafeVarargs
    public final void registerListener(PacketListener listener, Class<? extends Packet<?>>... packets) {
        dispatcher.register(listener, packets);
    }

    public void registerCommand(String name, CommandExecutor command) {
        try {
            CraftServer cs = (CraftServer) Bukkit.getServer();
            if (ClassProcessor.hasAnnotation(command)) ClassProcessor.process(command);
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            PluginCommand pluginCommand = constructor.newInstance(name, this);
            pluginCommand.setExecutor(command);
            cs.getCommandMap().register(name, pluginCommand);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public ConfigurationSection getModuleConfig(Manager manager) {
        return getConfig().getConfigurationSection(manager.getName());
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        Localization.loadLocalizations(getDataFolder());
        getManagers().forEach(m -> {
            ConfigurationSection section = getConfig().getConfigurationSection(m.getName());
            if (section != null) m.reloadConfig(section);
        });
    }
}