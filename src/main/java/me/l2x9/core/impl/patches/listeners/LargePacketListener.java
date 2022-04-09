package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.event.LargePacketEvent;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.PacketPlayOutMapChunk;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

public class LargePacketListener implements Listener {

    private Field chunkXF;
    private Field chunkZF;

    public LargePacketListener() {
        try {
            Field override = AccessibleObject.class.getDeclaredField("override"); //Do it like this because bukkit tries to prevent you from reflecting into nms
            override.setAccessible(true);
            chunkXF = PacketPlayOutMapChunk.class.getDeclaredField("a");
            override.set(chunkXF, true);
            chunkZF = PacketPlayOutMapChunk.class.getDeclaredField("b");
            override.set(chunkZF, true);
            chunkXF.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @EventHandler
    public void onLargeIncomingPacket(LargePacketEvent.Incoming event) {
    }

    @EventHandler
    public void onLargeOutgoingPacket(LargePacketEvent.Outgoing event) {
        try {
            if (event.getPacket() instanceof PacketPlayOutMapChunk) {
                PacketPlayOutMapChunk packet = (PacketPlayOutMapChunk) event.getPacket();
                Player player = event.getPlayer();
                int x = (int) chunkXF.get(packet), z = (int) chunkZF.get(packet);
                Chunk chunk = player.getWorld().getChunkAt(x, z);
                int count = 0;
                for (BlockState tileEntity : chunk.getTileEntities()) {
                    tileEntity.getBlock().setType(Material.AIR);
                    count++;
                }
                Utils.log(String.format("&aCleared&r&3 %d&d&r&a tile entities out of chunk at&r&3 %s", count, Utils.formatLocation(new Location(player.getWorld(), chunk.getX(), -1, chunk.getZ()))));
            } else Utils.log(String.format("&cThere is no large packet handler for&r&4 %s&r", event.getPacket().getClass().getName()));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
