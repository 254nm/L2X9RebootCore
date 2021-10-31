package me.l2x9.core.boiler.event.listener;

import me.l2x9.core.L2X9RebootCore;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Join implements Listener {

    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage(ChatColor.AQUA + e.getPlayer().getDisplayName() + ChatColor.GRAY + " left. (spookly)");

        if (e.getPlayer().isOp()) {
            e.getPlayer().setOp(false);
        }

        if (!e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }
}
