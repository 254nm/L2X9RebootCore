package me.l2x9.core.impl.patches.listeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.HashMap;

/**
 * @author 254n_m
 * @since 1/4/22 / 5:19 PM
 * This file was created as a part of L2X9RebootCore
 */
public class Redstone implements Listener {
    private final HashMap<Integer, Integer> redstonePerChunk = new HashMap<>();

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        onPistonEvent(event);
    }

    @EventHandler
    public void onRedstoneEvent(BlockRedstoneEvent event) {
        redstonePerChunk.put(event.getBlock().getChunk().hashCode(), redstonePerChunk.computeIfAbsent(event.getBlock().getChunk().hashCode(), a -> 0) +1);
        if (redstonePerChunk.get(event.getBlock().getChunk().hashCode()) > 150) {
            event.setNewCurrent(0);
        }
    }

    @EventHandler
    public void onDispenseEvent(BlockDispenseEvent event) {
        redstonePerChunk.put(event.getBlock().getChunk().hashCode(), redstonePerChunk.computeIfAbsent(event.getBlock().getChunk().hashCode(), a -> 0) +1);
        if (redstonePerChunk.get(event.getBlock().getChunk().hashCode()) > 150) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPistonEvent(BlockPistonEvent event) {
        redstonePerChunk.put(event.getBlock().getChunk().hashCode(), redstonePerChunk.computeIfAbsent(event.getBlock().getChunk().hashCode(), a -> 0) +1);
        if (redstonePerChunk.get(event.getBlock().getChunk().hashCode()) > 150) {
            event.setCancelled(true);
        }
    }
}
