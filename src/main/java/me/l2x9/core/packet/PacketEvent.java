package me.l2x9.core.packet;

import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.entity.Player;

public abstract class PacketEvent {
    private final Player player;
    private Packet<?> packet;
    private boolean cancelled;
    public PacketEvent(Packet<?> packet, Player player) {
        this.packet = packet;
        this.player = player;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public Player getPlayer() {
        return player;
    }

    public static class Incoming extends PacketEvent {
        public Incoming(Packet<?> packet, Player player) {
            super(packet, player);
        }
    }

    public static class Outgoing extends PacketEvent {
        public Outgoing(Packet<?> packet, Player player) {
            super(packet, player);
        }
    }
}
