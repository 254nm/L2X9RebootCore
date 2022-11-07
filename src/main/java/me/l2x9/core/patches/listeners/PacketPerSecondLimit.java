package me.l2x9.core.patches.listeners;

import me.l2x9.core.ViolationManager;
import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketListener;
import me.l2x9.core.util.Utils;

public class PacketPerSecondLimit extends ViolationManager implements PacketListener {
    private final int PP_LIMIT = 300;

    public PacketPerSecondLimit() {
        super(1, 300);
    }

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
        increment(event.getPlayer().getUniqueId().hashCode());
        int vls = getVLS(event.getPlayer().getUniqueId().hashCode());
        if (vls > PP_LIMIT) {
            remove(event.getPlayer().getUniqueId().hashCode());
            Utils.run(() -> Utils.kick(event.getPlayer(), "kick_pps_limit"));
        }
    }

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {
    }
}
