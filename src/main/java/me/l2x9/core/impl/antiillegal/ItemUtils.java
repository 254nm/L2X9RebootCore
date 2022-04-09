package me.l2x9.core.impl.antiillegal;

import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class ItemUtils {
    public static boolean isShulker(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta() instanceof BlockStateMeta && ((BlockStateMeta) item.getItemMeta()).getBlockState() instanceof ShulkerBox;
    }
}
