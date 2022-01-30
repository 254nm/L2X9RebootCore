package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.ViolationManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class LeverRateLimit extends ViolationManager implements Listener {

    public LeverRateLimit() {
        super(1);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() == Material.LEVER) {
            Player player = event.getPlayer();
            increment(player.hashCode());
            if (getVLS(player.hashCode()) > 20) {
                player.kickPlayer("Fuck off degen");
                remove(player.hashCode());
            }
        }
    }
}
