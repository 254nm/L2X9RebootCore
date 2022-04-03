package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.packet.PacketEvent;
import me.l2x9.core.packet.PacketListener;
import net.minecraft.server.v1_12_R1.PacketPlayInChat;
import net.minecraft.server.v1_12_R1.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PacketFly implements PacketListener {

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
        if (!(event.getPacket() instanceof PacketPlayInFlying)) return;
        PacketPlayInFlying packet = (PacketPlayInFlying) event.getPacket();
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        double y = packet.b(-1D);
        if (y == -1D) return;
        double yDelta = loc.getY() - y;
        if (Math.abs(yDelta) > 20) {
            event.setCancelled(true);
            player.teleport(player.getLocation()); //Sync the location after cancelling the packet
        }
    }

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {

    }
}
