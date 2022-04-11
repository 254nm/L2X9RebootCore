package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.ViolationManager;
import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketListener;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.*;

import java.util.Arrays;
import java.util.List;

public class PacketPerSecondLimit extends ViolationManager implements PacketListener {
    private final int PP_LIMIT = 225;

    public PacketPerSecondLimit() {
        super(1, 225);
    }

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
        increment(event.getPlayer().getUniqueId().hashCode());
        int vls = getVLS(event.getPlayer().getUniqueId().hashCode());
        if (vls > PP_LIMIT) {
            remove(event.getPlayer().getUniqueId().hashCode());
            Utils.run(() -> Utils.kick(event.getPlayer(), "&cYou are sending packets too fast!"));
        }
    }

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {
    }
}
