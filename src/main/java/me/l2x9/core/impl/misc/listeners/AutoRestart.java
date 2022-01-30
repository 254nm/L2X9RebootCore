package me.l2x9.core.impl.misc.listeners;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.boiler.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoRestart {

    public AutoRestart() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(() -> {
            Bukkit.getScheduler().runTask(L2X9RebootCore.getInstance(), () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getLocale().toLowerCase().contains("es")) {
                        player.kickPlayer(Utils.translateChars("&6[&3l2&bx9&6] El servidor se está reiniciando"));
                    } else
                        player.kickPlayer(Utils.translateChars("&6[&3l2&bx9&6] The server is restarting"));
                }
                Bukkit.getServer().shutdown();
            });
        }, 1, TimeUnit.DAYS);
    }
}
