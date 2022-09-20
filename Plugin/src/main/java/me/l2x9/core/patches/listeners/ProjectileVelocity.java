package me.l2x9.core.patches.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

/**
 * @author 254n_m
 * @since 2022-11-02 / 12:25 p.m.
 * This file was created as a part of L2X9RebootCore
 */
public class ProjectileVelocity implements Listener {
    @EventHandler
    public void onProjectile(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        Projectile projectile = event.getEntity();
        event.setCancelled(projectile.getVelocity().lengthSquared() > 10);
    }
}
