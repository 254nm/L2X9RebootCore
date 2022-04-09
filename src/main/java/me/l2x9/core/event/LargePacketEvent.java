package me.l2x9.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
@Getter
public abstract class LargePacketEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Packet<?> packet;
    private final PacketDataSerializer buf;
    private final int length;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static class Incoming extends LargePacketEvent {
        public Incoming(Player player, Packet<?> packet, PacketDataSerializer buf, int length) {
            super(player, packet, buf, length);
        }
    }

    public static class Outgoing extends LargePacketEvent {

        public Outgoing(Player player, Packet<?> packet, PacketDataSerializer buf, int length) {
            super(player, packet, buf, length);
        }
    }
}
