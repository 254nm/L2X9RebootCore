package me.l2x9.core.misc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OldSchoolKill implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) return;
        if (event.getMessage().split(" ")[0].equalsIgnoreCase("/kill")) {
            player.setHealth(0);
            event.setCancelled(true);
        }
    }
}
