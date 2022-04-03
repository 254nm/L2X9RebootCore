package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.packet.PacketEvent;
import me.l2x9.core.packet.PacketListener;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketPlayOutMapChunk;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 254n_m
 * @since 2021-11-02 / 9:50 p.m.
 * This file was created as a part of L2X9RebootCore
 */
public class ChunkBan implements PacketListener {
    private Field nbtF;

    public ChunkBan() {
        try {
            nbtF = PacketPlayOutMapChunk.class.getDeclaredField("e");
            nbtF.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }


    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {
        PacketPlayOutMapChunk packet = (PacketPlayOutMapChunk) event.getPacket();
        List<NBTTagCompound> tileEntities = (List<NBTTagCompound>) nbtF.get(packet);
        double dataSize = tileEntities.stream().
                map(o -> o.toString().length()).
                collect(Collectors.toList()).
                stream().
                mapToDouble(Integer::doubleValue).
                sum();
        int maxSize = 2097152;
        if (dataSize >= maxSize) {
            tileEntities.clear();
            Utils.log(String.format("&aPrevented large ChunkMapPacket near %s", Utils.formatLocation(event.getPlayer().getLocation())));
        }
    }
}
