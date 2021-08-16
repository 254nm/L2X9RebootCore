package ftp.sh.core.home.listeners;

import ftp.sh.core.home.HomeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {
    private final HomeManager main;

    public JoinListener(HomeManager main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        main.getHomeUtil().loadHomes(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        main.getHomeUtil().getHomes().remove(event.getPlayer().getUniqueId());
    }
}
