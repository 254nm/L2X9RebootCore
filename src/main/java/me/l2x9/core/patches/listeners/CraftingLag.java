package me.l2x9.core.patches.listeners;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CraftingLag implements Listener {
    private final ConcurrentHashMap<UUID, Integer> map = new ConcurrentHashMap<>();

    public CraftingLag() {
        Bukkit.getScheduler().runTaskTimer(L2X9RebootCore.getInstance(), () -> map.forEach((key, val) -> {
            if (val == 9) {
                map.remove(key);
                return;
            }
            if (val > 9) {
                map.replace(key, val - 9);
            }
        }), 0, 20);
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        for (HumanEntity player : event.getViewers()) {
            if (!map.containsKey(player.getUniqueId())) {
                map.put(player.getUniqueId(), 0);
            } else map.replace(player.getUniqueId(), map.get(player.getUniqueId()) + 1);
            if (map.get(player.getUniqueId()) > 2000) {
                Bukkit.getScheduler().runTask(L2X9RebootCore.getInstance(), () -> ((Player) player).kickPlayer("Fuck off degen"));
                Utils.log("&3Kicked&r&a " + player.getName() + "&r&3 for crafting lag");
                map.remove(player.getUniqueId());
                break;
            }
        }
    }
}
