package me.l2x9.core.misc.listeners;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import lombok.RequiredArgsConstructor;
import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.misc.MiscManager;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityEnderCrystal;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderCrystal;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

@RequiredArgsConstructor
public class CrystalSlowdown implements Listener {
    private final MiscManager manager;
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof EnderCrystal)) return;
        EntityEnderCrystal crystal = ((CraftEnderCrystal)event.getEntity()).getHandle();
        System.out.println(crystal.a);
        event.setCancelled(crystal.a < manager.getConfig().getInt("ACSlowdown"));
    }
    @EventHandler
    public void onCrystalSpawn(EntityAddToWorldEvent event) { //For some reason NMS sets the ticks lived counter to a random number in the crystal constructor
        if (event.getEntity() instanceof EnderCrystal) {
            ((CraftEnderCrystal)event.getEntity()).getHandle().a = 0;
        }
    }
}
