package me.l2x9.core;

import me.l2x9.core.impl.chat.ChatManager;
import me.l2x9.core.impl.command.CommandManager;
import me.l2x9.core.boiler.event.EventBus;
import me.l2x9.core.boiler.event.listener.PlayerJoinListener;
import me.l2x9.core.impl.home.HomeManager;
import me.l2x9.core.impl.misc.MiscManager;
import me.l2x9.core.impl.patches.PatchManager;
import me.l2x9.core.impl.randomspawn.RandomSpawnManager;
import me.l2x9.core.impl.tablist.TabManager;
import me.l2x9.core.boiler.util.ConfigCreator;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class L2X9RebootCore extends JavaPlugin {
    public static EventBus EVENT_BUS = new EventBus();
    private static L2X9RebootCore instance;
    private final long startTime = System.currentTimeMillis();
    private ScheduledExecutorService service;
    private List<ViolationManager> violationManagers;
    private List<Manager> managers;
    private ConfigCreator creator;

    public static L2X9RebootCore getInstance() {
        return instance;
    }

    public long getStartTime() {
        return startTime;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public ConfigCreator getCreator() {
        return creator;
    }

    @Override
    public void onEnable() {
        instance = this;
        managers = new ArrayList<>();
        violationManagers = new ArrayList<>();
        service = Executors.newScheduledThreadPool(4);
        service.scheduleAtFixedRate(() -> violationManagers.forEach(ViolationManager::decrementAll), 0, 1, TimeUnit.SECONDS);
        creator = new ConfigCreator(getName());
        creator.makeConfig(null, "config.yml", "config");
        registerListener(new PlayerJoinListener());
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
        managers.forEach(m -> m.destruct(this));
        managers.clear();
        violationManagers.clear();
        service.shutdown();
    }

    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public void registerListener(me.l2x9.core.boiler.event.Listener listener) {
        EVENT_BUS.subscribe(listener);
    }
}