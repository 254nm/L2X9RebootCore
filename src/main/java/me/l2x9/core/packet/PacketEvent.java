package me.l2x9.core.packet;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Getter
@Setter
public abstract class PacketEvent {
    @NonNull
    private Packet<?> packet;
    @NonNull
    private final Player player;

    private boolean cancelled;

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
