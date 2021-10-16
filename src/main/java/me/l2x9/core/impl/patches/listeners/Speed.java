package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.boiler.event.CustomEventHandler;
import me.l2x9.core.boiler.event.Listener;
import me.l2x9.core.boiler.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.PacketPlayInFlying;
import org.bukkit.Location;

import java.lang.reflect.Field;

public class Speed implements Listener {
    private Field ppifX;
    private Field ppifY;
    private Field ppifZ;

    public Speed() {
        Class<?> clazz = PacketPlayInFlying.class;
        try {
            ppifX = clazz.getDeclaredField("x");
            ppifY = clazz.getDeclaredField("y");
            ppifZ = clazz.getDeclaredField("z");
            ppifX.setAccessible(true);
            ppifY.setAccessible(true);
            ppifZ.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private double calcSpeed(PacketPlayInFlying packet) {
        Location from = null;
        Location to = null;
        if (from == null || to == null) throw new IllegalArgumentException("Invalid event passed to calcSpeed()");
        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();
        return Math.hypot(deltaX, deltaZ);
    }

    @CustomEventHandler
    public void onPlayerMove(PacketEvent.Incoming event) {
        if (event.getPacket() instanceof PacketPlayInFlying) {
            PacketPlayInFlying packet = (PacketPlayInFlying) event.getPacket();
        }
    }

    private Location constructLoc(PacketPlayInFlying packet) {

    }
}
