package me.l2x9.core.randomspawn.listeners;

import me.l2x9.core.randomspawn.RandomSpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Random;

public class RespawnListener implements Listener {
    private final Random rand = new Random();
    private final RandomSpawnManager main;

    public RespawnListener(RandomSpawnManager main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        if (event.isBedSpawn()) return;
        int attemptCount = 0;
        Location respawn = null;
        while (respawn == null) {
            attemptCount++;
            if (attemptCount == 600) {
                respawn = calcSpawnLocation(true);
            } else {
                respawn = calcSpawnLocation(false);
            }
        }
        event.setRespawnLocation(respawn);
    }

    private Location calcSpawnLocation(boolean lastAttempt) {
        int x = rand.nextInt(main.getRange()), z = rand.nextInt(main.getRange());
        World world = Bukkit.getWorld(main.getWorld());
        int y = world.getHighestBlockYAt(x, z);
        if (!lastAttempt) {
            Block blockAt = world.getBlockAt(x, y, z);
            if (main.getIgnored().contains(blockAt.getType())) return null;
        }
        return new Location(world, x, y, z);
    }
}
