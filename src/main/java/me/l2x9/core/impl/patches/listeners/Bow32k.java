package me.l2x9.core.impl.patches.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class Bow32k implements Listener {
    @EventHandler
    public void onArrow(ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Arrow) {
            if (e.getEntity().getVelocity().lengthSquared() > 11) {
                e.setCancelled(true);
            }
        }
    }
}
