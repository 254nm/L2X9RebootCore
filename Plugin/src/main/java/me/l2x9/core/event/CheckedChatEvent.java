package me.l2x9.core.event;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class CheckedChatEvent extends AsyncPlayerChatEvent {
    public CheckedChatEvent(boolean async, Player who, String message, Set<Player> players) {
        super(async, who, message, players);
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {
        throw new UnsupportedOperationException("This event cannot be cancelled");
    }
}
