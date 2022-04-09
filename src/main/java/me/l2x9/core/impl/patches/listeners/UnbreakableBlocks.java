package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.packet.PacketEvent;
import me.l2x9.core.packet.PacketListener;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockChange;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class UnbreakableBlocks implements PacketListener {

    private Field posF;
    private final List<Material> unmodifiable = Arrays.asList(Material.BEDROCK);

    public UnbreakableBlocks() {
        try {
            posF = PacketPlayOutBlockChange.class.getDeclaredField("a");
            posF.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {

    }

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {
        PacketPlayOutBlockChange packet = (PacketPlayOutBlockChange) event.getPacket();
        BlockPosition pos = (BlockPosition) posF.get(packet);
        Block block = event.getPlayer().getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        Material type = block.getType();
        if (unmodifiable.contains(block.getType())) {
            Bukkit.getScheduler().runTaskLater(L2X9RebootCore.getInstance(), () -> {
                Block newBlock = block.getWorld().getBlockAt(block.getLocation());
                if (newBlock.getType() != type) return;
                Utils.log(String.format("&aPrevented a&r&3 %s &r&afrom being broken at&r&3 %s&r", type.toString().toLowerCase(), Utils.formatLocation(block.getLocation())));
                block.setType(type);
            }, 3);
        }
    }
}
