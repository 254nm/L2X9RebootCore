package me.l2x9.core.antiillegal.check;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public abstract class Check {
    public abstract void check(Entity entity);

    public abstract void check(Inventory inventory);

    public abstract void check(PlayerInventory playerInventory);

    public abstract boolean check(ItemStack item);
}
