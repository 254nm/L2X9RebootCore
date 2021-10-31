package me.l2x9.core.impl.misc.listeners;

import me.l2x9.core.L2X9RebootCore;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Leave implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(ChatColor.AQUA + event.getPlayer().getDisplayName() + ChatColor.GRAY + " joined. (spookly)");
    }
}
