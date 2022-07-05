package me.l2x9.core.chat.listeners;

import me.l2x9.core.chat.ChatInfo;
import me.l2x9.core.chat.ChatManager;
import me.l2x9.core.util.Cooldown;
import me.l2x9.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.List;

public class ChatListener implements Listener {
    private final ChatManager manager;
    private final Cooldown cooldown;
    private final HashSet<String> tlds;


    public ChatListener(ChatManager manager, HashSet<String> tlds) {
        this.manager = manager;
        this.tlds = tlds;
        cooldown = new Cooldown();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        String ogMessage = event.getMessage();
        if (cooldown.checkCooldown(player)) {
            cooldown.setCooldown(player, manager.getConfig().getInt("Cooldown"));
        } else return;
        if (blockedCheck(ogMessage)) {
            player.sendMessage("<" + player.getDisplayName() + "> " + ogMessage);
            Utils.log("&3Prevented&r&a %s&r&3 from sending a message that has banned words", player.getName());
            return;
        }
        if (domainCheck(ogMessage)) {
            player.sendMessage("<" + player.getDisplayName() + "> " + ogMessage);
            Utils.log("&3Prevented player&r&a %s&r&3 from sending a link / server ip", player.getName());
            return;
        }
        String message = (ogMessage.startsWith(">")) ? "<" + player.getDisplayName() + "> " + ChatColor.GREEN + ogMessage : "<" + player.getDisplayName() + "> " + ogMessage;
        Bukkit.getLogger().info(message);
        for (Player online : Bukkit.getOnlinePlayers()) {
            ChatInfo info = manager.getInfo(online);
            if (info == null) continue;
            if (info.isIgnoring(player.getUniqueId()) || info.isToggledChat()) continue;
            online.sendMessage(message);
        }
    }

    private boolean domainCheck(String message) {
        message = message.toLowerCase().replace("dot", ".").replace("d0t", ".");
        if (message.indexOf('.') == -1) return false;
        String[] split = message.trim().split("\\.");
        if (split.length == 2) {
            String possibleTLD = split[1];
            if (possibleTLD.contains("/")) possibleTLD = possibleTLD.substring(0, possibleTLD.indexOf("/"));
            return tlds.contains(possibleTLD);
        } else {
            for (String word : split) {
                if (word.contains("/")) word = word.substring(0, word.indexOf("/"));
                if (tlds.contains(word)) return true;
            }
        }
        return false;
    }

    private boolean blockedCheck(String message) {
        List<String> blocked = manager.getConfig().getStringList("Blocked");
        for (String blockedWord : blocked) {
            if (message.toLowerCase().contains(blockedWord)) return true;
        }
        return false;
    }
}
