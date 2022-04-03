package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.ViolationManager;
import me.l2x9.core.packet.PacketEvent;
import me.l2x9.core.packet.PacketListener;

public class PacketPerSecondLimit extends ViolationManager implements PacketListener {
    private final L2X9RebootCore plugin = L2X9RebootCore.getPlugin();
    private final int PP_LIMIT = 225;

    public PacketPerSecondLimit() {
        super(1, 225);
    }

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
        increment(event.getPlayer().hashCode());
        int vls = getVLS(event.getPlayer().hashCode());
        if (vls > PP_LIMIT) {
            remove(event.getPlayer().hashCode());
            plugin.getServer().getScheduler().runTask(plugin, () -> event.getPlayer().kickPlayer("You are sending packets too fast!"));
        }
    }

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {

    }
}
