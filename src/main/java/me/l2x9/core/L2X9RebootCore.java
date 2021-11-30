package me.l2x9.core;

import me.l2x9.core.boiler.event.EventBus;
import me.l2x9.core.boiler.event.listener.PlayerJoinListener;
import me.l2x9.core.boiler.util.ConfigCreator;
import me.l2x9.core.impl.chat.ChatManager;
import me.l2x9.core.impl.command.CommandManager;
import me.l2x9.core.impl.patches.listeners.MeCommand;
import me.l2x9.core.impl.home.HomeManager;
import me.l2x9.core.impl.misc.MiscManager;
import me.l2x9.core.impl.patches.PatchManager;
import me.l2x9.core.impl.randomspawn.RandomSpawnManager;
import me.l2x9.core.impl.tablist.TabManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static me.l2x9.core.ConfigManager.config;

public final class L2X9RebootCore extends JavaPlugin {
    private BukkitRunnable messages;
    private BukkitRunnable runnable;
    public static EventBus EVENT_BUS = new EventBus();
    private static L2X9RebootCore instance;
    private final long startTime = System.currentTimeMillis();
    private ScheduledExecutorService service;
    private List<ViolationManager> violationManagers;
    private List<Manager> managers;
    private ConfigCreator creator;
    private final HashMap<Chunk, Integer> redstonePerChunk = new HashMap<>();

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
        violationManagers = new ArrayList<>();
        service = Executors.newScheduledThreadPool(4);
        service.scheduleAtFixedRate(() -> violationManagers.forEach(ViolationManager::decrementAll), 0, 1, TimeUnit.SECONDS);
        creator = new ConfigCreator(getName());
        creator.makeConfig(null, "config.yml", "config");
        registerListener(new PlayerJoinListener());
        registerManagers();
        redstonePerChunk.clear();
        getServer().getPluginManager().registerEvents(new MeCommand(), this);

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                redstonePerChunk.clear();
            }
        };
        runnable.runTaskTimer(this, 20, 20);


        (messages = new BukkitRunnable() {
            public void run() {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6[&3l2&bx9&6] Join the discord! https://discord.gg/pmHrKHhR7C"));
            }
        }).runTaskTimer(this, 600*20, 600*20);
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

    public static boolean isShulker(ItemStack item) {
        switch (item.getType()) {
            case BLACK_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case SILVER_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case WHITE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
                return true;
            default:
                return false;
        }
    }

    public void revert(ItemStack item) {
        if (item != null) {
            // Warning: Enables a dupe exploit, reason why its disabled by default - Fix in a pr if you have time! - https://cdn.discordapp.com/attachments/810446565822038016/858851144750989312/jbIAkzJ9CU.mp4
            if (config.getBoolean("LookForIllegalsInShulkers") && isShulker(item)) {
                BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                ShulkerBox box = (ShulkerBox) meta.getBlockState();

                box.getInventory().forEach(this::revert);

                box.update();
                meta.setBlockState(box);
                item.setItemMeta(meta);
            }

            if (config.getBoolean("RevertStackedItems")) {
                if (config.getBoolean("OnlyRevertStacksForCertainItems")) {
                    for (String s : config.getStringList("RevertStackedItemsList")) {
                        if (item.getType().name().equals(s) && item.getAmount() > item.getMaxStackSize()) {
                            item.setAmount(item.getMaxStackSize());
                        }
                    }
                } else {
                    if (item.getAmount() > item.getMaxStackSize()) {
                        item.setAmount(item.getMaxStackSize());
                    }
                }
            }

            if (config.getBoolean("RemoveSpawnEggs") && item.getItemMeta() instanceof SpawnEggMeta)
                item.setAmount(0);

            if (config.getStringList("BANNED_BLOCKS").contains(item.getType().name()))
                item.setAmount(0);
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent e) {
        onPistonEvent(e);
    }

    @EventHandler
    public void onRedstoneEvent(BlockRedstoneEvent e) {
        redstonePerChunk.put(e.getBlock().getChunk(), redstonePerChunk.computeIfAbsent(e.getBlock().getChunk(), a -> 0) +1);
        if (redstonePerChunk.get(e.getBlock().getChunk()) > 150) {
            e.setNewCurrent(0);
        }
    }

    @EventHandler
    public void onDispenseEvent(BlockDispenseEvent e) {
        redstonePerChunk.put(e.getBlock().getChunk(), redstonePerChunk.computeIfAbsent(e.getBlock().getChunk(), a -> 0) +1);
        if (redstonePerChunk.get(e.getBlock().getChunk()) > 150) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonEvent(final BlockPistonEvent e) {
        redstonePerChunk.put(e.getBlock().getChunk(), redstonePerChunk.computeIfAbsent(e.getBlock().getChunk(), a -> 0) +1);
        if (redstonePerChunk.get(e.getBlock().getChunk()) > 150) {
            e.setCancelled(true);
        }
    }

    public void registerCommand(String name, CommandExecutor... commands) {
        for (CommandExecutor command : commands) {
            CraftServer cs = (CraftServer) Bukkit.getServer();
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