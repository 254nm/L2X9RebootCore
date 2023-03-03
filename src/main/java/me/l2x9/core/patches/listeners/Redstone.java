package me.l2x9.core.patches.listeners;

import me.l2x9.core.ViolationManager;
import me.l2x9.core.util.Utils;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
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

    public Redstone() {
        super(1, 300);
    }

    @EventHandler
    public void onRedstoneEvent(BlockRedstoneEvent event) {
        process(event);
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        process(event);
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        process(event);
    }


    private void process(BlockEvent event) {
        if (!(event instanceof Cancellable)) return;
        Cancellable c = (Cancellable) event;
        Block block = event.getBlock();
        int disableTPS = 13;

        increment(block.getChunk().hashCode());
        if (Utils.getTPS() < disableTPS && getVLS(block.getChunk().hashCode()) > 70) {
            c.setCancelled(true);
            if (shouldBreakBlock()) block.breakNaturally();
        } else {
            int vls = getVLS(block.getChunk().hashCode());
            if (vls > 20000) {
                if (shouldBreakBlock()) event.getBlock().breakNaturally();
                c.setCancelled(true);
            }
        }
    }

    private boolean shouldBreakBlock() {
        return random.nextInt(0, 10) == 1;
    }
}
