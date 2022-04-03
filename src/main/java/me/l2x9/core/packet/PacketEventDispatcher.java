package me.l2x9.core.packet;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PacketEventDispatcher {
    private final List<PacketListener> listeners;
    private final Plugin plugin;

    public PacketEventDispatcher(Plugin plugin) {
        this.plugin = plugin;
        listeners = new ArrayList<>();
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(plugin, this), plugin);
    }

    public void register(PacketListener listener) {
        if (listeners.contains(listener)) return;
        listeners.add(listener);
    }

    public void unregister(PacketListener listener) {
        if (!listeners.contains(listener)) return;
        listeners.remove(listener);
    }

    protected void dispatch(PacketEvent event) {
        if (event instanceof PacketEvent.Incoming) {
            listeners.forEach(l -> {
                try {
                    l.incoming((PacketEvent.Incoming) event);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });
        } else if (event instanceof PacketEvent.Outgoing) {
            listeners.forEach(l -> {
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
