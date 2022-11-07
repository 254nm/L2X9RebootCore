package me.l2x9.core.misc.listeners;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoRestart {

    public AutoRestart() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(() -> {
            Bukkit.getScheduler().runTask(L2X9RebootCore.getInstance(), () -> {
                for (Player player : Bukkit.getOnlinePlayers()) Utils.kick(player, "kick_restarting");
                Bukkit.getServer().shutdown();
            });
        }, 1, TimeUnit.DAYS);
    }
}
