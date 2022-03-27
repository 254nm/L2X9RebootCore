package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.ViolationManager;
import me.l2x9.core.boiler.event.CustomEventHandler;
import me.l2x9.core.boiler.event.Listener;
import me.l2x9.core.boiler.event.events.PacketEvent;

public class PacketPerSecondLimit extends ViolationManager implements Listener {
    private final L2X9RebootCore plugin = L2X9RebootCore.getPlugin();
    private final int PP_LIMIT = 225;

    public PacketPerSecondLimit() {
        super(1, 225);
    }

    @CustomEventHandler
    public void onPacket(PacketEvent.Incoming event) {
        increment(event.getPlayer().hashCode());
        int vls = getVLS(event.getPlayer().hashCode());
        if (vls > PP_LIMIT) {
            remove(event.getPlayer().hashCode());
            plugin.getServer().getScheduler().runTask(plugin, () -> event.getPlayer().kickPlayer("You are sending packets too fast!"));
        }
    }
}
