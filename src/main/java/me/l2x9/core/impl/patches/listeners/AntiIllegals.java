package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.ConfigManager;
import me.l2x9.core.boiler.util.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

public class AntiIllegals extends Utils implements Listener, ConfigManager {
    @EventHandler
    private void onInventoryInteractEvent(InventoryInteractEvent e) {
        config.getStringList("BANNED_BLOCKS").forEach(b -> {
            if (e.getInventory().contains(Material.getMaterial(b))) {
                e.getInventory().remove(Material.getMaterial(b));
                e.setCancelled(true);
            }
        });

        e.getInventory().forEach(plugin::revert);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {
        plugin.revert(e.getCurrentItem());
        e.getWhoClicked().getInventory().forEach(plugin::revert);
    }

    @EventHandler
    private void onInventoryPickup(InventoryPickupItemEvent e) {
        String item = e.getItem().getType().toString();
        if (config.getStringList("BANNED_BLOCKS").contains(item)) {
            e.getItem().remove();
        }

        e.getInventory().forEach(plugin::revert);
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e) {
        String block = e.getBlockPlaced().getType().toString();

        if (config.getStringList("BANNED_BLOCKS").contains(block)) {
            if (config.getBoolean("RemoveIllegalBlockOnPlace")) {
                config.getStringList("BANNED_BLOCKS").forEach(b -> {
                    if (e.getPlayer().getInventory().contains(Material.getMaterial(b))) {
                        if (Material.getMaterial(b).equals(Material.ENDER_PORTAL_FRAME) && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.EYE_OF_ENDER)) {
                            e.getPlayer().getInventory().remove(Material.getMaterial(b));
                        } else {
                            e.getPlayer().getInventory().remove(Material.getMaterial(b));
                            e.setCancelled(true);
                        }
                    }
                });
            }
        }

        e.getPlayer().getInventory().forEach(plugin::revert);
    }
}
