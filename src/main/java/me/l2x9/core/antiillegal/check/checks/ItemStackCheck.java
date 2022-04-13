package me.l2x9.core.antiillegal.check.checks;

import me.l2x9.core.antiillegal.ItemUtils;
import me.l2x9.core.antiillegal.check.Check;
import me.l2x9.core.util.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.List;

public class ItemStackCheck extends Check {

    private final List<Material> illegal = Arrays.asList(Material.BEDROCK);

    @Override
    public void check(Entity entity) {
        if (entity instanceof InventoryHolder) {
            InventoryHolder holder = (InventoryHolder) entity;
            check(holder.getInventory());
        }
    }

    @Override
    public void check(Inventory inventory) {
        if (inventory instanceof PlayerInventory) {
            check((PlayerInventory) inventory);
        } else for (ItemStack item : inventory.getContents()) {
            if (check(item)) inventory.remove(item);
        }
    }

    @Override
    public void check(PlayerInventory playerInventory) {
        check(playerInventory.getItemInOffHand());
        for (ItemStack item : playerInventory.getArmorContents()) {
//            if (check(item))
        }
        for (ItemStack item : playerInventory.getContents()) check(item);
        for (ItemStack item : playerInventory.getExtraContents()) check(item);
    }

    @Override
    public boolean check(ItemStack item) {
        if (item == null) return false;
        if (ItemUtils.isShulker(item)) {
//            BlockStateMeta bsm = check((BlockStateMeta) item.getItemMeta());
//            item.setItemMeta(bsm);
            return false;
        }
        if (illegal.contains(item.getType())) {
            Utils.log(String.format("&aFound and deleted&r&3 %s&r", item.getType().toString().toLowerCase()));
            return true;
        }
        return false;
    }

//    private BlockStateMeta check(BlockStateMeta bsm) {
//        if (bsm instanceof ShulkerBox) {
//            ShulkerBox box = (ShulkerBox) bsm;
//            for (ItemStack item : box.getInventory().getContents()) {
//                if (item == null) continue;
//                if (ItemUtils.isShulker(item)) {
//                    box.getInventory().remove(item);
//                    Utils.log("&aDeleted a nested shulker");
//                }
//            }
//            check(box.getInventory().getContents());
//            bsm.setBlockState(box);
//            return bsm;
//        }
//        return bsm;
//    }
}
