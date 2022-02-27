package me.l2x9.core;

import me.l2x9.core.boiler.event.EventBus;
import me.l2x9.core.boiler.event.listener.PlayerJoinListener;
import me.l2x9.core.boiler.util.ConfigCreator;
import me.l2x9.core.impl.chat.ChatManager;
import me.l2x9.core.impl.command.CommandManager;
import me.l2x9.core.impl.home.HomeManager;
import me.l2x9.core.impl.misc.MiscManager;
import me.l2x9.core.impl.patches.PatchManager;
import me.l2x9.core.impl.randomspawn.RandomSpawnManager;
import me.l2x9.core.impl.tablist.TabManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
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
    //TODO: On chunk load scan for illegals and add to list of done chunks also ignore new chunks

    public static L2X9RebootCore getInstance() {
        return instance;
    }

    public boolean isDebug() {
        if (System.getProperty("l2x9coredebug") == null) return false;
        return Boolean.parseBoolean(System.getProperty("l2x9coredebug"));
    }

    public long getStartTime() {
        return startTime;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public static L2X9RebootCore getPlugin() {
        return getPlugin(L2X9RebootCore.class);
    }

    public ConfigCreator getCreator() {
        return creator;
    }

    @Override
    public void onEnable() {
        instance = this;
        managers = new ArrayList<>();
        saveDefaultConfig();
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

    public void registerCommand(String name, CommandExecutor... commands) {
        CraftServer cs = (CraftServer) Bukkit.getServer();
        for (CommandExecutor command : commands) {
            cs.getCommandMap().register(name, new org.bukkit.command.Command(name) {
                @Override
                public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                    command.onCommand(sender, this, commandLabel, args);
                    return true;
                }
            });
        }
    }
}