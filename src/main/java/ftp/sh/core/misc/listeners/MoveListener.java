package ftp.sh.core.misc.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class MoveListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getPlayer().getLocation().getWorld().getEnvironment() != World.Environment.NETHER) return;
        if (event.getPlayer().getLocation().getY() > 127) {
            event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), event.getPlayer().getLocation().getX(), 125, event.getPlayer().getLocation().getZ()));
        }
    }
    @EventHandler
    public void onMove(VehicleMoveEvent event) {
        if (event.getVehicle().getLocation().getWorld().getEnvironment() != World.Environment.NETHER) return;
        if (event.getVehicle().getLocation().getY() > 127) {
            event.getVehicle().teleport(new Location(event.getVehicle().getWorld(), event.getVehicle().getLocation().getX(), 125, event.getVehicle().getLocation().getZ()));
        }
    }
}
