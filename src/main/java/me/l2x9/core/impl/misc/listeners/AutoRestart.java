package me.l2x9.core.impl.misc.listeners;

import me.l2x9.core.L2X9RebootCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Timer;
import java.util.TimerTask;

public class AutoRestart {
    public AutoRestart() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Thread thread = new Thread(() -> {
                    Bukkit.broadcastMessage(ChatColor.RED + "Server restarting in 5 seconds...");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Bukkit.getScheduler().runTask(L2X9RebootCore.getInstance(), () -> {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.getLocale().toLowerCase().contains("es")) {
                                player.kickPlayer(ChatColor.GREEN + "El servidor se está reiniciando");
                            } else player.kickPlayer(ChatColor.GREEN + "The server is restarting");
                        }
                        timer.cancel();
                    });
                });
                thread.start();
            }
        }, 43200000 /* 12 hours */, 43200000);
    }
}
