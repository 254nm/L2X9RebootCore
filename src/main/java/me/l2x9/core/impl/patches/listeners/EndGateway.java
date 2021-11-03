package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.boiler.util.Utils;
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
    public void EndGatewayTeleportProtection(VehicleMoveEvent event) {
        try {
            Vehicle vehicle = event.getVehicle();
            if (vehicle.getWorld().getEnvironment() == World.Environment.THE_END && vehicle.getPassengers().stream().anyMatch(e -> e instanceof Player)) {
                for (BlockFace face : BlockFace.values()) {
                    Block next = vehicle.getLocation().getBlock().getRelative(face);
                    if (next.getType() == Material.END_GATEWAY) {
                        Player player = (Player) vehicle.getPassengers().stream().filter(e -> e instanceof Player).findAny().orElse(null);
                        if (player == null) return;
                        vehicle.eject();
                        vehicle.remove();
                        int x = vehicle.getLocation().getBlockX();
                        int y = vehicle.getLocation().getBlockY();
                        int z = vehicle.getLocation().getBlockZ();
                        String worldString = vehicle.getWorld().getName();
                        Utils.log("&1Prevented&r&e " + player.getName() + "&r&1 at &r&e" + x + " " + y + " " + z + " &r&1in world&e " + worldString + " &r&1from crashing the server");
                    }
                }
            }
        } catch (Throwable ignored) {
        }
    }
}
