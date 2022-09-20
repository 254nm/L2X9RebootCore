package me.l2x9.core.patches.listeners;

import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketListener;
import me.txmc.protocolapi.reflection.GetField;
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

    @GetField(clazz = PacketPlayOutBlockChange.class, name = "a")
    private Field outPosF;
    @GetField(clazz = PacketPlayInBlockDig.class, name = "a")
    private Field posF;
    @GetField(clazz = PacketPlayInBlockDig.class, name = "c")
    private Field actionF;

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {
        PacketPlayOutBlockChange packet = (PacketPlayOutBlockChange) event.getPacket();
        BlockPosition position = (BlockPosition) outPosF.get(packet);
        Player player = event.getPlayer();
        Point packetP = new Point(position.getX(), position.getZ());
        Point playerP = new Point((int) player.getLocation().getX(), (int) player.getLocation().getZ());
        if (playerP.distance(packetP) > Bukkit.getServer().getViewDistance() * 16) {
            event.setCancelled(true);
        }
    }

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
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
