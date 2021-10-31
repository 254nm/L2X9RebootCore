package me.l2x9.core.impl.misc.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Join implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.AQUA + event.getPlayer().getDisplayName() + ChatColor.GRAY + " left. (spookly)");

        if (event.getPlayer().isOp()) {
            event.getPlayer().setOp(false);
        }

        if (!event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            event.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }
}
