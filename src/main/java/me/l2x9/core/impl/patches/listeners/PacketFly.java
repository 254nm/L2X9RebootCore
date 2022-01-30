package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.boiler.event.CustomEventHandler;
import me.l2x9.core.boiler.event.Listener;
import me.l2x9.core.boiler.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PacketFly implements Listener {

    @CustomEventHandler
    public void onPacket(PacketEvent.Incoming event) {
        if (!(event.getPacket() instanceof PacketPlayInFlying)) return;
        PacketPlayInFlying packet = (PacketPlayInFlying) event.getPacket();
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        double y = packet.b(-1D);
        if (y == -1D) return;
        double yDelta = loc.getY() - y;
        if (Math.abs(yDelta) > 20) {
            event.setCancelled(true);
        }
    }
}
