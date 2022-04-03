package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.ViolationManager;
import me.l2x9.core.impl.patches.PatchManager;
import me.l2x9.core.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

public class CraftingLag extends ViolationManager implements Listener {
    private final PatchManager manager;

    public CraftingLag(PatchManager manager) {
        super(9);
        this.manager = manager;
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        for (Player player : event.getViewers().toArray(new Player[0])) {
            increment(player.getEntityId());
            if (getVLS(player.getEntityId()) > manager.getConfig().getInt("CraftingLag.MaxVLS")) {
                Utils.kick(player, "");
            }
        }
    }
}
