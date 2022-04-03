package me.l2x9.core.packet;

import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

public class PacketEventDispatcher {
    private final HashMap<HashSet<Class<? extends Packet<?>>>, PacketListener> listeners;
    private final Plugin plugin;

    public PacketEventDispatcher(Plugin plugin) {
        this.plugin = plugin;
        listeners = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(plugin, this), plugin);
    }

    public void register(PacketListener listener, Class<? extends Packet<?>>[] listeningFor) {
        if (listeners.containsValue(listener)) return;
        listeners.put(new HashSet<>(Arrays.asList(listeningFor)), listener);
    }

    public void unregister(PacketListener listener) {
        if (!listeners.containsValue(listener)) return;
    }

    @SuppressWarnings(value = "unchecked")
    protected void dispatch(PacketEvent event) {
        Class<? extends Packet<?>> clazz = (Class<? extends Packet<?>>) event.getPacket().getClass();
        List<PacketListener> pl = listeners.keySet().stream().filter(s -> s.contains(clazz) || s.contains(null)).map(listeners::get).collect(Collectors.toList());
        if (event instanceof PacketEvent.Incoming) {
            pl.forEach(l -> {
                try {
                    l.incoming((PacketEvent.Incoming) event);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });
        } else if (event instanceof PacketEvent.Outgoing) {
            pl.forEach(l -> {
                try {
                    l.outgoing((PacketEvent.Outgoing) event);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });
        } else throw new IllegalArgumentException("PacketEvent is an abstract class");
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
