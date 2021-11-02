package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.boiler.event.Listener;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPortalEvent;

public class MinecartCrash implements Listener {
    @EventHandler
    public void onNiggerRetard(EntityPortalEvent event) {
        if (event instanceof Minecart) {
            event.setCancelled(true);
        }
    }
}
