package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.boiler.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ElytraSpeedLimit implements Listener {
    private int disableTPS = 15;

    @EventHandler
    public void onGlide(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!player.isGliding()) return;
        double speed = calcSpeed(event);
        if (speed > 3) {
            event.setTo(event.getFrom());
            player.setGliding(false);
            Utils.sendMessage(player, "&3You are going too fast with your elytra");
            Utils.log("&3Prevented&r&a " + player.getName() + " &r&3from going too fast with an elytra");
        }
        if (disableTPS <= 0) return;
        double tps = ((CraftServer) Bukkit.getServer()).getHandle().getServer().recentTps[0];
        if (tps < disableTPS) {
            event.setCancelled(true);
            player.setGliding(false);
            Utils.sendMessage(player, "&3Elytras are currently disabled due to server lag. They should be back shortly");
            removeElytra(player);
        }
    }


    private double calcSpeed(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        double hypotX = to.getX() - from.getX();
        double hypotZ = to.getZ() - from.getZ();
        return Math.hypot(hypotX, hypotZ);
    }

    private void removeElytra(Player player) {
        ItemStack chestPlate = player.getInventory().getChestplate();
        if (chestPlate == null) return;
        if (chestPlate.getType() == Material.AIR) return;
        if (chestPlate.getType() == Material.ELYTRA) {
            PlayerInventory inventory = player.getInventory();
            if (inventory.firstEmpty() == -1) {
                player.getWorld().dropItemNaturally(player.getLocation(), chestPlate);
            } else inventory.setItem(inventory.firstEmpty(), chestPlate);
            ItemStack[] buffer = inventory.getArmorContents();
            buffer[2] = null;
            inventory.setArmorContents(buffer);
        }
    }
}
