package me.l2x9.core.misc.epc;

import lombok.RequiredArgsConstructor;
import me.l2x9.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EntityCheckTask implements Runnable {
    private final HashMap<EntityType, Integer> entityPerChunk;

    @Override
    public void run() {
        for (Chunk[] chunks : Bukkit.getWorlds().stream().map(World::getLoadedChunks).collect(Collectors.toList())) {
            for (Chunk chunk : chunks) {
                entityPerChunk.forEach((e, i) -> {
                    Entity[] entities = Arrays.stream(chunk.getEntities()).filter(en -> en.getType() == e).collect(Collectors.toList()).toArray(Entity[]::new);
                    int amt = entities.length;
                    if (amt >= i) {
                        Utils.log("&3Removed &r&a%d&r&3 entities from chunk&r&a %d,%d", amt-i, chunk.getX(), chunk.getZ());
                        while (amt >= i) {
                            entities[0].remove();
                            amt--;
                        }
                    }
                });
            }
        }
    }
}
