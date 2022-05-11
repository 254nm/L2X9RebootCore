package me.l2x9.core.misc.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class MoveListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getWorld().getEnvironment() != World.Environment.NETHER) return;
        if (player.getLocation().getY() > 127) {
            player.teleport(new Location(player.getWorld(), player.getLocation().getX(), 125, player.getLocation().getZ()));
        }
    }
    @EventHandler
    public void onMove(VehicleMoveEvent event) {
        Vehicle vehicle = event.getVehicle();
        if (vehicle.getLocation().getWorld().getEnvironment() != World.Environment.NETHER) return;
        if (vehicle.getLocation().getY() > 127) {
            vehicle.teleport(new Location(vehicle.getWorld(), vehicle.getLocation().getX(), 125, vehicle.getLocation().getZ()));
        }
    }
}
