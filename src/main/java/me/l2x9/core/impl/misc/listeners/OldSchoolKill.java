package me.l2x9.core.impl.misc.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OldSchoolKill implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().isOp()) return;
        if (event.getMessage().split(" ")[0].equalsIgnoreCase("/kill")) {
            event.getPlayer().setHealth(0);
            event.setCancelled(true);
        }
    }
}
