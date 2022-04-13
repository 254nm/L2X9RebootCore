package me.l2x9.core.patches.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @author 254n_m
 * @since 9/11/2021 / 9:34 PM
 * This file was created as a part of L2X9RebootCore
 */
public class Damage implements Listener {

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (event.getDamage() > 30D) {
            event.setCancelled(true);
            ((Player) event.getDamager()).damage(event.getDamage());
        }
    }
}
