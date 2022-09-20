package me.l2x9.core.patches.listeners;

import me.l2x9.core.util.Utils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class EndGateway implements Listener {
    @EventHandler
    public void onEndGateway(VehicleMoveEvent event) {
        Vehicle vehicle = event.getVehicle();
        if (vehicle.getWorld().getEnvironment() == World.Environment.THE_END && vehicle.getPassengers().stream().anyMatch(e -> e instanceof Player)) {
            for (BlockFace face : BlockFace.values()) {
                Block next = vehicle.getLocation().getBlock().getRelative(face);
                if (next.getType() == Material.END_GATEWAY) {
                    vehicle.eject();
                    vehicle.remove();
                    Player player = (Player) vehicle.getPassengers().stream().filter(e -> e instanceof Player).findAny().orElse(null);
                    if (player == null) return;
                    Utils.log(String.format("&1Prevented %s from crashing the server at %s)", player.getName(), Utils.formatLocation(vehicle.getLocation())));
                }
            }
        }
    }
}
