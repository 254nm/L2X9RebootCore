package me.l2x9.core.impl.patches.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class Bow32k implements Listener {
    @EventHandler
    public void onArrow(ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof  Arrow || e.getEntity() instanceof SpectralArrow) {
            if (e.getEntity().getVelocity().lengthSquared() > 11) {
                e.setCancelled(true);
                Player player = (Player) e.getEntity().getShooter();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Don't be a fucking faggot, I fixed the bypass lol. The plugin has texted Bleepo"));
            }
        }
    }
}
