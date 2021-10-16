package me.l2x9.core.impl.chat.listeners;

import me.l2x9.core.boiler.util.Cooldown;
import me.l2x9.core.impl.chat.ChatInfo;
import me.l2x9.core.impl.chat.ChatManager;
import me.l2x9.core.boiler.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class ChatListener implements Listener {
    private final ChatManager manager;
    private final Cooldown cooldown;


    public ChatListener(ChatManager manager) {
        this.manager = manager;
        cooldown = new Cooldown();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        boolean blocked = false;
        Player player = event.getPlayer();
        if (cooldown.checkCooldown(player)) {
            cooldown.setCooldown(player, manager.getConfig().getConfig().getInt("Cooldown"));
        } else {
            blocked = true;
        }
        if (!blocked) {
            List<String> list = manager.getConfig().getConfig().getStringList("Blocked");
            for (String word : list) {
                if (event.getMessage().toLowerCase().contains(word)) {
                    blocked = true;
                    player.sendMessage("<" + player.getName() + "> " + event.getMessage());
                    Utils.log("&3Prevented&r&a " + player.getName() + "&r&3 from advertising");
                    break;
                }
            }
        }
        if (blocked) return;
        Player chatter = event.getPlayer();
        String message = (event.getMessage().startsWith(">")) ? "<" + chatter.getName() + "> " + ChatColor.GREEN + event.getMessage() : "<" + chatter.getName() + "> " + event.getMessage();
        Bukkit.getLogger().info(message);
        for (Player online : Bukkit.getOnlinePlayers()) {
            ChatInfo info = manager.getInfo(online);
            if (info == null) continue;
            if (info.isIgnoring(chatter.getUniqueId()) || info.isToggledChat()) continue;
            online.sendMessage(message);
        }
    }
}
