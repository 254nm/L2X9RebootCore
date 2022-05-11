package me.l2x9.core.home.listeners;

import lombok.AllArgsConstructor;
import me.l2x9.core.home.HomeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class JoinListener implements Listener {
    private final HomeManager main;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        main.getHomeIO().loadHomes(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        main.getHomeIO().getHomes().remove(event.getPlayer().getUniqueId());
    }
}
