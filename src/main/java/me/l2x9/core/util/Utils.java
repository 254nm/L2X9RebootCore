package me.l2x9.core.util;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.MissingResourceException;
import java.util.logging.Level;

public class Utils {
    private static final DecimalFormat format = new DecimalFormat("#.##");
    private static final String PREFIX = "&7&r&b&3L2X9&r&aCore&r&7&r";

    public static String translateChars(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String getFormattedInterval(long ms) {
        long seconds = ms / 1000L % 60L;
        long minutes = ms / 60000L % 60L;
        long hours = ms / 3600000L % 24L;
        long days = ms / 86400000L;
        return String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds);
    }

    public static ChatColor getTPSColor(String input) {
        if (!input.equals("*20")) {
            String toDouble = input.split("\\.")[0];
            double tps = Double.parseDouble(toDouble);
            if (tps >= 18.0D) {
                return ChatColor.GREEN;
            } else {
                return tps >= 13.0D ? ChatColor.YELLOW : ChatColor.RED;
            }
        } else {
            return ChatColor.GREEN;
        }
    }

    /**
     * Runs a task on bukkit's main thread
     *
     * @param runnable The task to be run
     */
    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(L2X9RebootCore.getInstance(), runnable);
    }

    /**
     * Will attempt to invoke a method called sendMessage(String.class) on the object given
     *
     * @param obj     The recipient
     * @param message The message to be sent
     */
    public static void sendMessage(Object obj, String message) {
        sendMessage(obj, message, true);
    }

    public static void kick(Player player, String message) {
        message = String.format("%s &7->&r %s", PREFIX, message);
        message = translateChars(message);
        player.kickPlayer(message);
    }

    public static void log(String format, Object... args) {
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        String message = String.format(format, args);
        message = translateChars(message);
        L2X9RebootCore.getInstance().getLogger().log(Level.INFO, String.format("%s%c%s", message, Character.MIN_VALUE, element.getClassName()));
    }

    public static void log(String message, Manager manager) {
        message = translateChars(message);
        message = String.format("[&3%s&r] %s", manager.getName(), message);
        L2X9RebootCore.getInstance().getLogger().log(Level.INFO, message);
    }

    public static void crashPlayer(Player target) {
        for (int i = 0; i < 100; i++) {
            target.spawnParticle(Particle.EXPLOSION_HUGE, target.getLocation(), Integer.MAX_VALUE, 1, 1, 1);
        }
    }

    public static String formatLocation(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        World world = location.getWorld();
        return "&3world&r&a " + world.getName() + " &r&3X:&r&a " + format.format(x) + " &r&3Y:&r&a " + format.format(y) + " &r&3Z:&r&a " + format.format(z);
    }

    public static ItemStack shallowReadItemStack(PacketDataSerializer buf) {
        short itemID = buf.readShort();
        if (itemID < 0) return net.minecraft.server.v1_12_R1.ItemStack.a;
        byte count = buf.readByte();
        short damage = buf.readShort();
        return new net.minecraft.server.v1_12_R1.ItemStack(Item.getById(itemID), count, damage);
    }

    public static void clearCurrentContainer(EntityPlayer player) {
        if (!(player.activeContainer instanceof ContainerChest)) return;
        IInventory inventory = ((ContainerChest) player.activeContainer).e();
        for (int i = 0; i < inventory.getSize(); i++) inventory.setItem(i, ItemStack.a);
        inventory.update();
        log(String.format("&aCleared inventory&r&3 %s&r&a with window ID&r&3 %d&r&a because it had excessive NBT data", inventory.getClass().getSimpleName(), player.activeContainer.windowId));
    }

    public static String getPrefix() {
        return PREFIX;
    }

    public static void sendMessage(Object obj, String msg, boolean prefix) {
        if (prefix) msg = String.format("%s &7➠&r %s", PREFIX, msg);
        msg = translateChars(msg);
        try {
            Method method = obj.getClass().getMethod("sendMessage", String.class);
            method.setAccessible(true);
            method.invoke(obj, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void unpackResource(String resourceName, File file) {
        try {
            InputStream is = L2X9RebootCore.class.getClassLoader().getResourceAsStream(resourceName);
            if (is == null) throw new NullPointerException(String.format("Resource %s is not present in the jar", resourceName));
            Files.copy(is, file.toPath());
            is.close();
        } catch (Throwable t) {
            log("&cFailed to extract resource from jar due to &r&3 %s&r&c! Please see the stacktrace below for more info", t.getMessage());
            t.printStackTrace();
        }
    }
}

