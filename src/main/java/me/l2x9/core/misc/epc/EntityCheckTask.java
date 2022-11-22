package me.l2x9.core.misc.epc;

import lombok.RequiredArgsConstructor;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityEnderCrystal;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderCrystal;
import org.bukkit.entity.EnderCrystal;
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
                if (chunk.getEntities().length == 0) continue;
                entityPerChunk.forEach((e, i) -> {
                    Entity[] entities = Arrays.stream(chunk.getEntities()).filter(en -> en.getType() == e).collect(Collectors.toList()).toArray(Entity[]::new);
                    int amt = entities.length;
                    if (amt >= i) {
                        Utils.log("&3Removed &r&a%d&r&3 entities from chunk&r&a %d,%d&r&3 in world &r&a&s", amt-i, chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
                        while (amt >= i) {
                            if (entities[0] instanceof EnderCrystal) { //The bukkit api cant remove ender crystals for some reason?
                                EntityEnderCrystal crystal = ((CraftEnderCrystal)entities[0]).getHandle();
                                crystal.damageEntity(DamageSource.GENERIC, 10);
                                amt--;
                                continue;
                            }
                            entities[0].remove();
                            amt--;
                        }
                    }
                });
            }
        }
    }
}
