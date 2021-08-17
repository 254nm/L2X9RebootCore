package me.l2x9.core.patches.listeners;

import me.l2x9.core.event.CustomEventHandler;
import me.l2x9.core.event.Listener;
import me.l2x9.core.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class PacketFly implements Listener {
    private Field xF;
    private Field yF;
    private Field zF;
    private Field yawF;
    private Field pitchF;
    public PacketFly() {
        try {
            xF = PacketPlayInFlying.class.getDeclaredField("x");
            yF = PacketPlayInFlying.class.getDeclaredField("y");
            zF = PacketPlayInFlying.class.getDeclaredField("z");
            yawF = PacketPlayInFlying.class.getDeclaredField("yaw");
            pitchF = PacketPlayInFlying.class.getDeclaredField("pitch");
            xF.setAccessible(true);
            yF.setAccessible(true);
            zF.setAccessible(true);
            yawF.setAccessible(true);
            pitchF.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
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
