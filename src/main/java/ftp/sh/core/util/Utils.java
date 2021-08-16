package ftp.sh.core.util;

import ftp.sh.core.L2X9RebootCore;
import ftp.sh.core.Manager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.logging.Level;

public class Utils {
    private static final DecimalFormat format = new DecimalFormat("#.##");

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
                return tps >= 13.0D && tps < 18.0D ? ChatColor.YELLOW : ChatColor.RED;
            }
        } else {
            return ChatColor.GREEN;
        }
    }

    public static void sendMessage(Object obj, String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        if (obj instanceof Player) {
            Player player = (Player) obj;
            player.sendMessage(message);
        } else if (obj instanceof CommandSender) {
            CommandSender sender = (CommandSender) obj;
            sender.sendMessage(message);
        }
    }

    public static void log(String message) {
        message = translateChars(message);
        L2X9RebootCore.getInstance().getLogger().log(Level.INFO, message);
    }

    public static void log(String message, Manager manager) {
        message = translateChars(message);
        message = "[" + manager.getName() + "]" + message;
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
        return "&3world&r&a " + world.getName()
                + " &r&3X:&r&a " + format.format(x)
                + " &r&3Y:&r&a " + format.format(y)
                + " &r&3Z:&r&a " + format.format(z);
    }
}
