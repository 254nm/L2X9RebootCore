package me.l2x9.core.patches.listeners;

import me.l2x9.core.ViolationManager;
import me.l2x9.core.util.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;

import java.util.Arrays;

/**
 * @author 254n_m
 * @since 5/27/22/ 9:14 PM
 * This file was created as a part of L2X9RebootCore
 */
public class EntityCollideListener extends ViolationManager implements Listener {
    public EntityCollideListener() {
        super(1);
    }

    @EventHandler
    public void onCollide(VehicleEntityCollisionEvent event) {
        if (event.getEntity().getVehicle() == event.getVehicle()) return;
        Vehicle vehicle = event.getVehicle();
        increment(vehicle.getChunk().hashCode());
        if (getVLS(vehicle.getChunk().hashCode()) > 5000) {
            Vehicle[] vehicles = Arrays.stream(vehicle.getChunk().getEntities()).filter(e -> e instanceof Vehicle).toArray(Vehicle[]::new);
            Arrays.stream(vehicles).forEach(Entity::remove);
            Utils.log(String.format("&aRemoved&r&3 %d&r&a vehicles from a chunk at &r&3%s", vehicles.length, Utils.formatLocation(vehicle.getLocation())));
            remove(vehicle.getChunk().hashCode());
        }
    }
}
