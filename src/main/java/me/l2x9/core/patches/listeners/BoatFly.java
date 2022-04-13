package me.l2x9.core.patches.listeners;

import me.l2x9.core.util.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class BoatFly implements Listener {
    private final ArrayList<Player> list = new ArrayList<>();
    int time;
    //TODO Rewrite this dogshit code

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        time++;
        Player player = event.getPlayer();
        double ax = 0.0D;
        double ay = 0.0D;
        double az = 0.0D;
        if (time == 2) {
            ax = player.getLocation().getX();
            ay = player.getLocation().getY();
            az = player.getLocation().getZ();
            time = 0;
        }
        double x = 0.0D;
        double y = 0.0D;
        double z = 0.0D;
        if (player.isInsideVehicle() && player.getVehicle() instanceof Boat) {
            Boat boat = (Boat) player.getVehicle();
            Material m = boat.getLocation().getBlock().getType();
            if (m != Material.STATIONARY_WATER && m != Material.WATER && !boat.isOnGround()) {
                if (!list.contains(player)) {
                    list.add(player);
                    x = player.getLocation().getX();
                    y = player.getLocation().getY();
                    z = player.getLocation().getZ();
                }
                if ((boat.getVelocity().getY() > 0.0D || boat.getVelocity().getY() < 0.12D) && list.contains(player))
                    if (x != 0.0D && y != 0.0D && z != 0.0D) {
                        boat.teleport(new Location(boat.getWorld(), x, y, z));
                        player.teleport(new Location(player.getWorld(), x, y + 0.5D, z));
                        boat.remove();
                        Utils.log("&3Prevented&r&a " + player.getName() + "&r&3 from boatflying");
                        list.remove(player);
                    } else {
                        boat.teleport(new Location(boat.getWorld(), ax, ay, az));
                        player.teleport(new Location(player.getWorld(), ax, ay, az));
                    }
            }
        }
    }
}
