package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.packet.PacketEvent;
import me.l2x9.core.packet.PacketListener;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayInBlockDig;
import net.minecraft.server.v1_12_R1.PacketPlayInBlockDig.EnumPlayerDigType;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockChange;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class NoCom implements PacketListener {
    private final List<EnumPlayerDigType> validActions = Arrays.asList(EnumPlayerDigType.ABORT_DESTROY_BLOCK, EnumPlayerDigType.START_DESTROY_BLOCK, EnumPlayerDigType.STOP_DESTROY_BLOCK);
    private Field outPosF;
    private Field posF;
    private Field actionF;

    public NoCom() {
        try {
            outPosF = PacketPlayOutBlockChange.class.getDeclaredField("a");
            outPosF.setAccessible(true);
            posF = PacketPlayInBlockDig.class.getDeclaredField("a");
            posF.setAccessible(true);
            actionF = PacketPlayInBlockDig.class.getDeclaredField("c");
            actionF.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {
        if (event.getPacket() instanceof PacketPlayOutBlockChange) {
            PacketPlayOutBlockChange packet = (PacketPlayOutBlockChange) event.getPacket();
            BlockPosition position = (BlockPosition) outPosF.get(packet);
            Player player = event.getPlayer();
            Point packetP = new Point(position.getX(), position.getZ());
            Point playerP = new Point((int) player.getLocation().getX(), (int) player.getLocation().getZ());
            if (playerP.distance(packetP) > Bukkit.getServer().getViewDistance() * 16) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
        if (event.getPacket() instanceof PacketPlayInBlockDig) {
            PacketPlayInBlockDig packet = (PacketPlayInBlockDig) event.getPacket();
            BlockPosition position = (BlockPosition) posF.get(packet);
            Player player = event.getPlayer();
            EnumPlayerDigType action = (EnumPlayerDigType) actionF.get(packet);
            if (!validActions.contains(action)) return;
            Point playerP = new Point(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
            Point packetP = new Point(position.getX(), position.getZ());
            if (playerP.distance(packetP) > Bukkit.getServer().getViewDistance() * 16) {
                event.setCancelled(true);
            }
        }
    }
}
