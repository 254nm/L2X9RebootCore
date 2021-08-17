package me.l2x9.core.misc.listeners;

import me.l2x9.core.event.Listener;
import me.l2x9.core.event.events.PacketEvent;
import me.l2x9.core.event.CustomEventHandler;
import net.minecraft.server.v1_12_R1.MathHelper;
import net.minecraft.server.v1_12_R1.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class TestPhase implements Listener {
    private final BlockFace[] faces = new BlockFace[]{BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST};

    @CustomEventHandler
    public void onMove(PacketEvent.Incoming event) {
        if (!(event.getPacket() instanceof PacketPlayInFlying)) return;
        PacketPlayInFlying packet = (PacketPlayInFlying) event.getPacket();
        Location to = new Location(event.getPlayer().getWorld(), packet.a(-1D), packet.b(-1D), packet.c(-1D));
        if (to.getX() == -1D || to.getY() == -1D || to.getZ() == -1D) return;
        Location from = event.getPlayer().getLocation();
        if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
            if (from.getBlock().getType() == Material.AIR || from.getBlock().getType().toString().contains("STEP") || !from.getBlock().getType().isSolid())
                return;
            BlockFace facing = getDirection(event.getPlayer());
            Block rel = from.getBlock().getRelative(facing);
            if (rel.getType() != Material.AIR) {
                from.setYaw(event.getPlayer().getLocation().getYaw());
                from.setPitch(event.getPlayer().getLocation().getPitch());
                event.getPlayer().teleport(from);
            }
        }
    }

    private BlockFace getDirection(Player player) {
        double rotation = player.getLocation().getYaw();
        return getByIndex(MathHelper.floor((rotation * 4.0F / 360.0F) + 0.5D) & 3);
    }

    private BlockFace getByIndex(int horizontalIndexIn) {
        return faces[Math.abs(horizontalIndexIn % faces.length)];
    }
}
