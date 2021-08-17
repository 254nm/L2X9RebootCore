package me.l2x9.core.boiler.event.events;

import me.l2x9.core.boiler.event.Cancellable;
import me.l2x9.core.boiler.event.Event;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.entity.Player;

public abstract class PacketEvent extends Event implements Cancellable {
    private final Player player;
    private boolean cancel;
    private Packet<?> packet;

    public PacketEvent(Packet<?> packet, Player player) {
        this.packet = packet;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
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
