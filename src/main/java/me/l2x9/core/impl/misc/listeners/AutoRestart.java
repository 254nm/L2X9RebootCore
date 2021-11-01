package me.l2x9.core.impl.misc.listeners;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.boiler.util.TimerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Timer;
import java.util.TimerTask;

public class AutoRestart {
    TimerUtil time = new TimerUtil();

    private void broadCast(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6[&3l2&bx9&6] " + message));
    }
    public AutoRestart() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Thread thread = new Thread(() -> {
                    broadCast("Restarting in 5 minutes");
                    time.delay(240000);
                    broadCast("Restarting in 1 minute");
                    time.delay(30000);
                    broadCast("Restarting in 30 seconds");
                    time.delay(25000);
                    broadCast("Restarting in 5 seconds");
                    time.delay(1000);
                    broadCast("Restarting in 4 seconds");
                    time.delay(1000);
                    broadCast("Restarting in 3 seconds");
                    time.delay(1000);
                    broadCast("Restarting in 2 seconds");
                    time.delay(1000);
                    broadCast("Restarting in 1 second");
                    Bukkit.getScheduler().runTask(L2X9RebootCore.getInstance(), () -> {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.getLocale().toLowerCase().contains("es")) {
                                player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&6[&3l2&bx9&6] El servidor se está reiniciando"));
                            } else player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&6[&3l2&bx9&6] The server is restarting"));
                        }
                        Bukkit.getServer().shutdown();
                        timer.cancel();
                    });
                });
                thread.start();
            }
        }, 86400000, 86400000 /*86400000  24 hours  86400000*/);
    }
}
