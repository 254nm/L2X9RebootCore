package me.l2x9.core.patches.listeners;

import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketEventDispatcher;
import me.txmc.protocolapi.PacketListener;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.ItemBow;
import net.minecraft.server.v1_12_R1.PacketPlayInFlying;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PacketFly implements PacketListener {

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
        PacketPlayInFlying packet = (PacketPlayInFlying) event.getPacket();
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        double y = packet.b(-1D);
        if (y == -1D) return;
        double yDelta = loc.getY() - y;
        if (Math.abs(yDelta) > 10) {
            event.setCancelled(true);
            Utils.run(() -> player.teleport(player.getLocation())); //Sync the location after cancelling the packet
        }
    }

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {

    }
}
