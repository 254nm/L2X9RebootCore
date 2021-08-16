package ftp.sh.core.util;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class Cooldown {

    private final HashMap<Player, Double> map = new HashMap<>();

    public void setCooldown(Player player, int seconds) {
        double delay = System.currentTimeMillis() + (seconds * 1000L);
        map.put(player, delay);
    }

    public int getCooldown(Player player) {
        return Math.toIntExact(Math.round((map.get(player) - System.currentTimeMillis()) / 1000));
    }

    public boolean checkCooldown(Player player) {
        return !map.containsKey(player) || map.get(player) <= System.currentTimeMillis();
    }

}
