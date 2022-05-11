package me.l2x9.core.chat.listeners;

import me.l2x9.core.util.Cooldown;
import me.l2x9.core.chat.ChatInfo;
import me.l2x9.core.chat.ChatManager;
import me.l2x9.core.util.Utils;
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
            cooldown.setCooldown(player, manager.getConfig().getInt("Cooldown"));
        } else {
            blocked = true;
        }
        if (!blocked) {
            List<String> list = manager.getConfig().getStringList("Blocked");
            for (String word : list) {
                if (event.getMessage().toLowerCase().contains(word)) {
                    blocked = true;
                    player.sendMessage("<" + player.getDisplayName()+ "> " + event.getMessage());
                    Utils.log("&3Prevented&r&a " + player.getName() + "&r&3 from advertising");
                    break;
                }
            }
        }
        if (blocked) return;
        Player chatter = event.getPlayer();
        String message = (event.getMessage().startsWith(">")) ? "<" + chatter.getDisplayName() + "> " + ChatColor.GREEN + event.getMessage() : "<" + chatter.getDisplayName() + "> " + event.getMessage();
        Bukkit.getLogger().info(message);
        for (Player online : Bukkit.getOnlinePlayers()) {
            ChatInfo info = manager.getInfo(online);
            if (info == null) continue;
            if (info.isIgnoring(chatter.getUniqueId()) || info.isToggledChat()) continue;
            online.sendMessage(message);
        }
    }
}
