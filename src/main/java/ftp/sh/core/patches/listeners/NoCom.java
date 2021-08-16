package ftp.sh.core.patches.listeners;

import ftp.sh.core.event.CustomEventHandler;
import ftp.sh.core.event.Listener;
import ftp.sh.core.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockChange;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.awt.*;
import java.lang.reflect.Field;

public class NoCom implements Listener {
    private Field posF;

    public NoCom() {
        try {
            posF = PacketPlayOutBlockChange.class.getDeclaredField("a");
            posF.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @CustomEventHandler
    public void onPacket(PacketEvent.Outgoing event) {
        if (event.getPacket() instanceof PacketPlayOutBlockChange) {
            try {
                PacketPlayOutBlockChange packet = (PacketPlayOutBlockChange) event.getPacket();
                Player player = event.getPlayer();
                World world = ((CraftWorld) player.getWorld()).getHandle();
                BlockPosition position = (BlockPosition) posF.get(packet);
                Point playerPoint = new Point((int) player.getLocation().getX(), (int) player.getLocation().getZ());
                Point packetPoint = new Point(position.getX(), position.getZ());
                if (playerPoint.distance(packetPoint) > Bukkit.getServer().getViewDistance() * 16) {
                    event.setPacket(new PacketPlayOutBlockChange(world, new BlockPosition(player.getLocation().getX(), player.getLocation().getX(), player.getLocation().getX())));
                }
            } catch (Throwable t) {
            }
        }
    }
}
