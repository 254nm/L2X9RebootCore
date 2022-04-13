package me.l2x9.core.antiillegal.listeners;

import lombok.AllArgsConstructor;
import me.l2x9.core.antiillegal.check.Check;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

@AllArgsConstructor
public class InventoryOpen implements Listener {
    private final Check check;

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        System.out.println("Checking inv");
        check.check(event.getInventory());
    }
}
