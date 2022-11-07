package me.l2x9.core.misc.listeners;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinMessages implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        Bukkit.getOnlinePlayers().forEach(p -> Utils.sendLocalizedMessage(p, "join_message", false, player.getName()));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);
        Bukkit.getOnlinePlayers().forEach(p -> Utils.sendLocalizedMessage(p, "leave_message", false, player.getName()));

        if (player.isOp() && !L2X9RebootCore.getInstance().isDebug()) {
            player.setOp(false);
        }

        if (!player.getGameMode().equals(GameMode.SURVIVAL) && !L2X9RebootCore.getInstance().isDebug()) {
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
}
