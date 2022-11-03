package me.l2x9.core.misc.epc;

import lombok.RequiredArgsConstructor;
import me.l2x9.core.util.Utils;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.Arrays;
import java.util.HashMap;

@RequiredArgsConstructor
public class EntitySpawnListener implements Listener {
    private final HashMap<EntityType, Integer> entityPerChunk;

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (!entityPerChunk.containsKey(entity.getType())) return;
        int amt = enumerate(event.getLocation().getChunk(), entity.getType());
        int max = entityPerChunk.get(entity.getType());
        if (amt >= max) {
            event.setCancelled(true);
            Utils.log("&3Prevented a&r&a %s&r&3 from spawning (&r&a%d&r&3/&r&a%d&r&3)", entity.getType().toString().toLowerCase(), amt, max);
        }
    }

    private int enumerate(Chunk chunk, EntityType entityType) {
        return (int) Arrays.stream(chunk.getEntities()).filter(e -> e.getType() == entityType).count();
    }
}
