package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.event.LargePacketEvent;
import me.txmc.protocolapi.reflection.GetField;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.PacketPlayOutMapChunk;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;

public class LargePacketListener implements Listener {

    @GetField(clazz = PacketPlayOutMapChunk.class, name = "a")
    private Field chunkXF;
    @GetField(clazz = PacketPlayOutMapChunk.class, name = "b")
    private Field chunkZF;

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
