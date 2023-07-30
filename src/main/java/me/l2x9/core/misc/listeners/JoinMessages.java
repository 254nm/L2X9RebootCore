package me.l2x9.core.misc.listeners;

import me.l2x9.core.Localization;
import me.l2x9.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;

public class JoinMessages implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();

        Utils.sendLocalizedMessage(player, "welcome_message", true);

        String key = (!player.hasPlayedBefore()) ? "player_join_first_time" : "join_message";
        Bukkit.getOnlinePlayers().forEach(p -> Utils.sendLocalizedMessage(p, key, false, player.getName()));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        if (Arrays.stream(Thread.currentThread().getStackTrace()).map(StackTraceElement::toString).anyMatch(e -> e.contains("PlayerConnection.disconnect") || e.contains("Utils.kick"))) return;
        Bukkit.getOnlinePlayers().forEach(p -> Utils.sendLocalizedMessage(p, "leave_message", false, player.getName()));
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        event.setLeaveMessage(null);
        Player player = event.getPlayer();
        String reason = ChatColor.stripColor(event.getReason());
        Bukkit.getOnlinePlayers().forEach(p -> Utils.sendLocalizedMessage(p, "kick_message", false, player.getName(), reason));
    }
}
