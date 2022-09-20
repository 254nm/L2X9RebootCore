package me.l2x9.core.randomspawn.listeners;

import lombok.AllArgsConstructor;
import me.l2x9.core.randomspawn.RandomSpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class RespawnListener implements Listener {
    private final ThreadLocalRandom rand = ThreadLocalRandom.current();
    private final RandomSpawnManager main;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        if (event.isBedSpawn()) return;
        int attemptCount = 0;
        Location respawn = null;
        while (respawn == null) respawn = calcSpawnLocation(attemptCount++ == 600);
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
