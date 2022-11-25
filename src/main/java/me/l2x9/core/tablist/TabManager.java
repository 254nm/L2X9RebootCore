package me.l2x9.core.tablist;

import lombok.Getter;
import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Timer;

public class TabManager extends Manager {

    private final long startTime = L2X9RebootCore.getInstance().getStartTime();
    private Timer timer;
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
        double tps = ((CraftServer) Bukkit.getServer()).getServer().recentTps[0];
        String strTps = (tps >= 20) ? String.format("%s*20.0", ChatColor.GREEN) : String.format("%s%.2f", Utils.getTPSColor(tps), tps);
        String uptime = Utils.getFormattedInterval(System.currentTimeMillis() - startTime);
        String online = String.valueOf(Bukkit.getOnlinePlayers().size());
        String ping = String.valueOf(getPing(player));
        return Utils.translateChars(input.replace("%tps%", strTps).replace("%players%", online)).replace("%ping%", ping).replace("%uptime%", uptime);
    }

    private int getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }
}
