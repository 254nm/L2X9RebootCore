package me.l2x9.core.impl.tablist;

import lombok.Getter;
import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Timer;

public class TabManager extends Manager {

    private Timer timer;
    private final long startTime = L2X9RebootCore.getInstance().getStartTime();
    private final DecimalFormat format = new DecimalFormat("##.##");
    @Getter
    private ConfigurationSection config;

    public TabManager() {
        super("TabList");
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        timer = new Timer();
        config = plugin.getModuleConfig(this);
        timer.scheduleAtFixedRate(new TabRunnable(this), 0, 1000);
    }

    @Override
    public void destruct(L2X9RebootCore plugin) {
        timer.cancel();
    }

    @Override
    public void reloadConfig(ConfigurationSection config) {
        this.config = config;
    }

    public String parsePlaceHolders(String input, Player player) {
        String tps = String.valueOf(format.format(((CraftServer) Bukkit.getServer()).getHandle().getServer().recentTps[0]));
        int ping = getPing(player);
        return Utils.translateChars(input.
                        replace("%tps%", Utils.getTPSColor(tps) + tps).
                        replace("%players%", Bukkit.getOnlinePlayers().size() + "")).
                replace("%ping%", ping + "").
                replace("%uptime%", Utils.getFormattedInterval(System.currentTimeMillis() - startTime)
                );
    }

    private int getPing(Player player) {
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        return nmsPlayer.ping;
    }
}
