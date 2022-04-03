package me.l2x9.core.packet;

public interface PacketListener {
    void incoming(PacketEvent.Incoming event) throws Throwable;

    void outgoing(PacketEvent.Outgoing event) throws Throwable;
}
