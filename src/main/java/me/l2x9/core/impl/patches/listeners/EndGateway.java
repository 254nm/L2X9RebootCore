package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.boiler.event.CustomEventHandler;
import me.l2x9.core.boiler.event.Listener;
import me.l2x9.core.boiler.event.events.PacketEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTeleportEvent;

public class EndGateway implements Listener, org.bukkit.event.Listener {
    boolean started;
    @CustomEventHandler
    public void onPacket(PacketEvent.Incoming event) {
        if (!started) return;
        System.out.println("Incoming event.getPacket() = " + event.getPacket());
    }
    @CustomEventHandler
    public void onPacket(PacketEvent.Outgoing event) {
        if (!started) return;
        System.out.println("Outgoing event.getPacket() = " + event.getPacket());
    }
    @EventHandler
    public void onTeleport(EntityTeleportEvent event) {
        if (event.getFrom().getBlock().getType() == Material.END_GATEWAY) {
            System.out.println("Crash started");
        }
    }
}
