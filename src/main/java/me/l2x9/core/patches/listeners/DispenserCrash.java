package me.l2x9.core.patches.listeners;

import me.l2x9.core.util.Utils;
import org.bukkit.block.Dispenser;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class DispenserCrash implements Listener {
    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        int height = event.getBlock().getY();
        Dispenser dispenser = (Dispenser) event.getBlock().getState();
        if (!hasShulker(dispenser)) return;
        if (height == 255 || height <= 1) {
            event.setCancelled(true);
            String players = String.join(" ", dispenser.getLocation().getWorld().getNearbyEntities(dispenser.getLocation(), 60, 60, 60).
                    stream().
                    filter(e -> e instanceof Player).
                    map(p -> "&r&a " + p.getName() + "&r&3,&r ").
                    toArray(String[]::new));
            Utils.log("&3Prevented a dispenser crash in " + Utils.formatLocation(dispenser.getLocation()) + "&r&3 nearby players&r&a " + players);
        }
    }

    private boolean hasShulker(Dispenser dispenser) {
        for (ItemStack item : dispenser.getInventory()) {
            if (item != null && isShulker(item)) {
                return true;
            }
        }
        return false;
    }

    private boolean isShulker(ItemStack item) {
        return item.hasItemMeta()
                && item.getItemMeta() instanceof BlockStateMeta
                && ((BlockStateMeta) item.getItemMeta()).getBlockState() instanceof ShulkerBox;
    }
}
