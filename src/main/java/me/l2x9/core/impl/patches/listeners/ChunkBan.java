package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.boiler.event.CustomEventHandler;
import me.l2x9.core.boiler.event.Listener;
import me.l2x9.core.boiler.event.events.PacketEvent;
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
public class ChunkBan implements Listener {
    private Field nbtF;
    private final int MAX_SIZE = 2097152;

    public ChunkBan() {
        try {
            nbtF = PacketPlayOutMapChunk.class.getDeclaredField("e");
            nbtF.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    @CustomEventHandler
    public void onPacketSending(PacketEvent.Outgoing event) {
        if (event.getPacket() instanceof PacketPlayOutMapChunk) {
            try {
                PacketPlayOutMapChunk packet = (PacketPlayOutMapChunk) event.getPacket();
                List<NBTTagCompound> tileEntities = (List<NBTTagCompound>) nbtF.get(packet);
                double dataSize = tileEntities.stream().
                        map(o -> o.toString().length()).
                        collect(Collectors.toList()).
                        stream().
                        mapToDouble(Integer::doubleValue).
                        sum();
                if (dataSize >= MAX_SIZE) tileEntities.clear();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
