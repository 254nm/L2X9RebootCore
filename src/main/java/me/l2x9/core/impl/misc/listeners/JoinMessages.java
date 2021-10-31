package me.l2x9.core.impl.misc.listeners;

import me.l2x9.core.L2X9RebootCore;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinMessages implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(ChatColor.AQUA + event.getPlayer().getDisplayName() + ChatColor.GRAY + " joined (spookly)");
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.AQUA + event.getPlayer().getDisplayName() + ChatColor.GRAY + " left (spookly)");

        if (event.getPlayer().isOp() && !L2X9RebootCore.getInstance().isDebug()) {
            event.getPlayer().setOp(false);
        }

        if (!event.getPlayer().getGameMode().equals(GameMode.SURVIVAL) && !L2X9RebootCore.getInstance().isDebug()) {
            event.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }
}
