package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.boiler.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class WitherLag implements Listener {
    private final Timer timer;
    private final HashMap<World, Integer> skulls = new HashMap<>();


    public WitherLag() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                countSkulls();
                for (World world : Bukkit.getWorlds()) {
                    List<Entity> withers = world.getEntities().stream().filter(e -> e instanceof Wither).collect(Collectors.toList());
                    if (withers.size() <= 80) continue;
                    int count = 0;
                    while (withers.size() >= 80) {
                        Wither wither = (Wither) withers.get(0);
                        count++;
                        wither.remove();
                        withers.remove(0);
                    }
                    Utils.log("&3Removed&r&a " + count + "&r&3 withers from world&r&a " + world.getName());
                }
            }
        }, 0, 10000);
    }

    public void cancelTimer() {
        timer.cancel();
    }

    @EventHandler
    public void onSkull(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof WitherSkull) {
            WitherSkull skull = (WitherSkull) event.getEntity();
            if (skulls.get(skull.getWorld()) > 80) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        List<Entity> skulls = event.getWorld().getEntities().stream().filter(e -> e instanceof WitherSkull).collect(Collectors.toList());
        skulls.forEach(Entity::remove);
    }

    private void countSkulls() {
        for (World world : Bukkit.getWorlds()) {
            if (!skulls.containsKey(world)) {
                skulls.put(world, world.getEntities().stream().filter(e -> e instanceof WitherSkull).toArray().length);
            } else {
                skulls.replace(world, world.getEntities().stream().filter(e -> e instanceof WitherSkull).toArray().length);
            }
        }
    }
}
