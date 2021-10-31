package me.l2x9.core.boiler.event.listener;

import me.l2x9.core.L2X9RebootCore;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Leave implements Listener {

    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(ChatColor.AQUA + e.getPlayer().getDisplayName() + ChatColor.GRAY + " joined. (spookly)");
    }
}
