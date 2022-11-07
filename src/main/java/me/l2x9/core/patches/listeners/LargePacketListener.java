package me.l2x9.core.patches.listeners;

import me.l2x9.core.event.LargePacketEvent;
import me.l2x9.core.util.Utils;
import me.txmc.protocolapi.reflection.GetField;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
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
                int count = chunk.getTileEntities().length;
                Utils.run(() -> {
                    for (BlockState tileEntity : chunk.getTileEntities()) {
                        tileEntity.getBlock().setType(Material.AIR);
                    }
                });
                Utils.log(String.format("&aCleared&r&3 %d&d&r&a tile entities out of chunk at&r&3 %s", count, Utils.formatLocation(new Location(player.getWorld(), chunk.getX(), -1, chunk.getZ()))));
            } else if (event.getPacket() instanceof PacketPlayOutWindowItems) {
                Utils.clearCurrentContainer(((CraftPlayer) event.getPlayer()).getHandle());
            } else
                Utils.log(String.format("&cThere is no large packet handler for&r&4 %s&r", event.getPacket().getClass().getName()));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @EventHandler
    public void onLargeIncomingPacket(LargePacketEvent.Incoming event) {
        try {
            PacketDataSerializer buf = event.getBuf();
            if (event.getPacket() instanceof PacketPlayInSetCreativeSlot) {
                Utils.run(() -> {
                    Player player = event.getPlayer();
                    player.getInventory().clear();
                    Utils.sendPrefixedLocalizedMessage(player, "bookban_inventory_clear");
                });
            } else if (event.getPacket() instanceof PacketPlayInWindowClick) {
                buf.readBytes(6);
                buf.g();
                ItemStack itemFromWire = Utils.shallowReadItemStack(buf);
                Utils.run(() -> {
                    EntityPlayer player = ((CraftPlayer) event.getPlayer()).getHandle();
                    if (player.activeContainer.windowId == 0) {
                        for (int i = 0; i < player.inventory.getSize(); i++) {
                            ItemStack item = player.inventory.getItem(i);
                            if (item.getItem() != itemFromWire.getItem()) continue;
                            player.inventory.setItem(i, ItemStack.a);
                        }
                        player.updateInventory(player.activeContainer);
                        Utils.sendPrefixedLocalizedMessage(player.getBukkitEntity(), "bookban_removed_item", itemFromWire.getItem().getName().toLowerCase().replace("_", "-"));
                    } else {
                        Utils.clearCurrentContainer(player);
                    }
                });
            } else
                Utils.log(String.format("&cThere is no large packet handler for&r&4 %s&r", event.getPacket().getClass().getName()));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
