package me.l2x9.core.chat.listeners;

import lombok.RequiredArgsConstructor;
import me.l2x9.core.chat.ChatManager;
import me.l2x9.core.util.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

@RequiredArgsConstructor
public class CommandWhitelist implements Listener {
    private final ChatManager main;
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().isOp()) return;
        List<String> allowedCommands = main.getConfig().getStringList("CommandWhitelist");
        String command = event.getMessage().split(" ")[0];
        event.setCancelled(allowedCommands.stream().noneMatch(a -> a.equalsIgnoreCase(command)));
        if (event.isCancelled()) Utils.sendOptionalPrefixMessage(event.getPlayer(), String.format("&cThe command&r&a %s&r&c is not allowed", command), true);
    }
}
