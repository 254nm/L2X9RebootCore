package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class MeCommand implements Listener, ConfigManager {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage();
        List<String> disabled = Arrays.asList("/plugins", "/pl");

        if (e.getMessage().contains("/me")) {
            Player player = e.getPlayer();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Degenerate :)"));
            Arrays.stream(Sound.values()).forEach(sound -> player.playSound(player.getLocation(), sound, Integer.MAX_VALUE, 1.0F));
            e.setCancelled(true);
        }

        if (e.getMessage().toLowerCase().contains("/?")) {
            Player player = e.getPlayer();
            plugin.getLogger().log(Level.INFO, "Prevented " + player.getDisplayName() + " from seeing the plugins (/?)");
            player.sendMessage(ChatColor.RED + "Please use /help instead");
            e.setCancelled(true);
        }

        if (e.getMessage().contains(":")) {
            Player player = e.getPlayer();
            plugin.getLogger().log(Level.INFO, "Prevented " + player.getDisplayName() + " from seeing the plugins (/bukkit:help)");
            player.sendMessage(ChatColor.RED + "Degenerate");
            e.setCancelled(true);
        }

        if (command.length() == disabled.get(0).length()) {
            command = command.substring(0, disabled.get(0).length());
        } else {
            if (command.length() == disabled.get(1).length()) {
                command = command.substring(0, disabled.get(1).length());
            }
        }

        if (disabled.contains(command.toLowerCase())) {
            String message = ChatColor.translateAlternateColorCodes('&', "&6I see that you want to see the plugins :) The only plugin is L2X9RebootCore :L");
            e.getPlayer().sendMessage(message);
            plugin.getLogger().log(Level.INFO, "Prevented " + e.getPlayer().getName() + " from seeing the plugins (/pl)");
            e.setCancelled(true);
        }
    }
}