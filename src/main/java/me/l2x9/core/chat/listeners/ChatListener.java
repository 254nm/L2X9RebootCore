package me.l2x9.core.chat.listeners;

import lombok.RequiredArgsConstructor;
import me.l2x9.core.Localization;
import me.l2x9.core.chat.ChatInfo;
import me.l2x9.core.chat.ChatManager;
import me.l2x9.core.event.CheckedChatEvent;
import me.l2x9.core.util.Utils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class ChatListener implements Listener {
    private final ChatManager manager;
    private final HashSet<String> tlds;
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event instanceof CheckedChatEvent) return;
        event.setCancelled(true);
        Player sender = event.getPlayer();
        int cooldown = manager.getConfig().getInt("Cooldown");
        ChatInfo ci = manager.getInfo(sender);
        if (ci.isChatLock()) {
            Utils.sendPrefixedLocalizedMessage(sender, "chat_cooldown", cooldown);
            return;
        }
        ci.setChatLock(true);
        service.schedule(() -> ci.setChatLock(false), cooldown, TimeUnit.SECONDS);
        String ogMessage = event.getMessage();
        String playerName = sender.getDisplayName() + ChatColor.RESET;
        String message = format(ogMessage, playerName);
        if (blockedCheck(ogMessage)) {
            sender.sendMessage(message);
            Utils.log("&3Prevented&r&a %s&r&3 from sending a message that has banned words", sender.getName());
            return;
        }
        if (domainCheck(ogMessage)) {
            sender.sendMessage(message);
            Utils.log("&3Prevented player&r&a %s&r&3 from sending a link / server ip", sender.getName());
            return;
        }
        Bukkit.getLogger().info(message);
        HashMap<String, String> translationCache = (manager.getTranslator() == null) ? null : new HashMap<>();
        String messageLanguage = manager.getTranslator().detectLanguage(ogMessage).join();
        for (Player online : Bukkit.getOnlinePlayers()) {
            ChatInfo info = manager.getInfo(online);
            if (info == null) continue;
            if (info.isIgnoring(sender.getUniqueId()) || info.isToggledChat()) continue;

            String recipientLocale = online.getLocale().split("_")[0];
            if (manager.getTranslator() != null && info.isAutoTranslate() && manager.getTranslator().getSupportedLanguages().contains(recipientLocale) &&
                    manager.getTranslator().getSupportedLanguages().contains(messageLanguage) && !messageLanguage.equalsIgnoreCase(recipientLocale)) {

                Localization recipientLocalization = Localization.getLocalization(online.getLocale());
                String translatedText;
                if (!translationCache.containsKey(recipientLocale)) {
                    translatedText = manager.getTranslator().translate(ogMessage, "auto", recipientLocale).join();
                    translationCache.put(recipientLocale, translatedText);
                } else translatedText = translationCache.get(recipientLocale);
                TextComponent msg = new TextComponent(format(translatedText, playerName));
                msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(String.format(recipientLocalization.get("message_auto_translated"), ogMessage)).create()));
                online.spigot().sendMessage(msg);

            } else online.sendMessage(message);
        }
        Bukkit.getPluginManager().callEvent(new CheckedChatEvent(event.isAsynchronous(), sender, message, event.getRecipients()));
    }

    private boolean domainCheck(String message) {
        if (!manager.getConfig().getBoolean("PreventLinks")) return false;
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
            if (message.toLowerCase().contains(blockedWord.toLowerCase())) return true;
        }
        return false;
    }

    private String format(String message, String playerName) {
        return (message.startsWith(">")) ? String.format("<%s>%s %s", playerName, ChatColor.GREEN, message) : String.format("<%s> %s", playerName, message);
    }
}
