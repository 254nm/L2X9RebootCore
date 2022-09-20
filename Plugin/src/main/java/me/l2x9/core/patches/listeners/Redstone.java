package me.l2x9.core.patches.listeners;

import me.l2x9.core.ViolationManager;
import net.minecraft.server.v1_12_R1.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author 254n_m
 * @since 1/4/22 / 5:19 PM
 * This file was created as a part of L2X9RebootCore
 */
public class Redstone extends ViolationManager implements Listener {
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final DedicatedServer server = ((CraftServer) Bukkit.getServer()).getHandle().getServer();
    private final int disableTPS = 13;

    public Redstone() {
        super(1, 300);
    }

    @EventHandler
    public void onRedstoneEvent(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        increment(block.getChunk().hashCode());
        if (server.recentTps[0] < disableTPS && getVLS(block.getChunk().hashCode()) > 70) {
            event.setNewCurrent(0);
            if (shouldBreakBlock()) block.breakNaturally();
        } else {
            int vls = getVLS(block.getChunk().hashCode());
            if (vls > 20000) {
                if (shouldBreakBlock()) event.getBlock().breakNaturally();
                event.setNewCurrent(0);
            }
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        processPiston(event);
    }
    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        processPiston(event);
    }

    private void processPiston(BlockPistonEvent event) {
        Block block = event.getBlock();
        increment(block.getChunk().hashCode());
        if (server.recentTps[0] < disableTPS && getVLS(block.getChunk().hashCode()) > 70) {
            event.setCancelled(true);
            if (shouldBreakBlock()) block.breakNaturally();
        } else {
            int vls = getVLS(block.getChunk().hashCode());
            if (vls > 20000) {
                if (shouldBreakBlock()) event.getBlock().breakNaturally();
                event.setCancelled(true);
            }
        }
    }

    private boolean shouldBreakBlock() {
        return random.nextInt(0, 10) == 1;
    }
}
