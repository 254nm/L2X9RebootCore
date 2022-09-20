package me.l2x9.core;

import lombok.Getter;
import me.l2x9.core.chat.ChatManager;
import me.l2x9.core.command.CommandManager;
import me.l2x9.core.home.HomeManager;
import me.l2x9.core.misc.MiscManager;
import me.l2x9.core.patches.PatchManager;
import me.l2x9.core.randomspawn.RandomSpawnManager;
import me.l2x9.core.tablist.TabManager;
import me.txmc.protocolapi.PacketEventDispatcher;
import me.txmc.protocolapi.PacketListener;
import me.txmc.protocolapi.reflection.ClassProcessor;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class L2X9RebootCore extends JavaPlugin {
    @Getter
    private static L2X9RebootCore instance;
    @Getter
    private final long startTime = System.currentTimeMillis();
    private PacketEventDispatcher dispatcher;
    private ScheduledExecutorService service;
    private List<ViolationManager> violationManagers;
    @Getter
    private List<Manager> managers;
    //TODO: On chunk load scan for illegals and add to list of done chunks also ignore new chunks

    public boolean isDebug() {
        if (System.getProperty("l2x9coredebug") == null) return false;
        return Boolean.parseBoolean(System.getProperty("l2x9coredebug"));
    }

    @Override
    public void onEnable() {
        instance = this;
        loadMixins();
        dispatcher = new PacketEventDispatcher(this);
        managers = new ArrayList<>();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getLogger().addHandler(new LoggerHandler());
        saveDefaultConfig();
        violationManagers = new ArrayList<>();
        service = Executors.newScheduledThreadPool(4);
        service.scheduleAtFixedRate(() -> violationManagers.forEach(ViolationManager::decrementAll), 0, 1, TimeUnit.SECONDS);
        registerManagers();
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

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
        managers.forEach(m -> m.destruct(this));
        managers.clear();
        violationManagers.clear();
        service.shutdown();
    }

    private void loadMixins() {
        File mixinJar = new File(".", "mixins-temp.jar");
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("mixins.dat");
            if (is == null) throw new RuntimeException("The plugin jar is missing the mixins");
            Files.copy(is, mixinJar.toPath());
            URLClassLoader ccl = new URLClassLoader(new URL[]{mixinJar.toURI().toURL()});
            Class<?> mixinMainClass = Class.forName(String.format("%s.mixin.MixinMain", getClass().getPackage().getName()), true, ccl);
            Object instance = mixinMainClass.newInstance();
            Method mainM = instance.getClass().getDeclaredMethod("init", JavaPlugin.class);
            mainM.invoke(instance, this);
        } catch (Throwable t) {
            getLogger().severe(String.format("Failed to load mixins due to %s. Please see the stacktrace below for more info", t.getClass().getName()));
            t.printStackTrace();
        } finally {
            if (mixinJar.exists()) mixinJar.delete();
        }
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
        getManagers().forEach(m -> {
            ConfigurationSection section = getConfig().getConfigurationSection(m.getName());
            if (section != null) m.reloadConfig(section);
        });
    }
}