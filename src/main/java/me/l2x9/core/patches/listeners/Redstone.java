package me.l2x9.core.patches.listeners;

import lombok.RequiredArgsConstructor;
import me.l2x9.core.ViolationManager;
import me.l2x9.core.patches.PatchManager;
import me.l2x9.core.util.Utils;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
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
    private final PatchManager main;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public Redstone(PatchManager main) {
        super(1, 300);
        this.main = main;
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
        ConfigurationSection config = main.getConfig().getConfigurationSection("Redstone");
        Block block = event.getBlock();
        int vls = getVLS(block.getChunk().hashCode());

        increment(block.getChunk().hashCode());
        if (Utils.getTPS() < config.getInt("StrictTPS") && vls > config.getInt("StrictMaxVLS")) {
            cancelEvent(event);
            if (shouldBreakBlock()) block.breakNaturally();
        } else {
            if (vls > config.getInt("RegularMaxVLS")) {
                if (shouldBreakBlock()) event.getBlock().breakNaturally();
                cancelEvent(event);
            }
        }
    }
    private void cancelEvent(BlockEvent event) {
        if (event instanceof BlockRedstoneEvent) {
            ((BlockRedstoneEvent)event).setNewCurrent(0);
        } else ((Cancellable)event).setCancelled(true);
    }

    private boolean shouldBreakBlock() {
        return random.nextInt(0, 10) == 1;
    }
}
