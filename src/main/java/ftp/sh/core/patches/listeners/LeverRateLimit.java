package ftp.sh.core.patches.listeners;

import ftp.sh.core.ViolationManager;
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
            increment(player.getUniqueId());
            if (getVLS(player.getUniqueId()) > 20) {
                player.kickPlayer("Fuck off degen");
                remove(player.getUniqueId());
            }
        }
    }
}
