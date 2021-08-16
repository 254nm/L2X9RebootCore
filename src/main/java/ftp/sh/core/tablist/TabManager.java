package ftp.sh.core.tablist;

import ftp.sh.core.L2X9RebootCore;
import ftp.sh.core.util.IOUtil;
import ftp.sh.core.Manager;
import ftp.sh.core.util.ConfigCreator;
import ftp.sh.core.util.ConfigCreator.ConfigurationWrapper;
import ftp.sh.core.util.Utils;
import net.minecraft.server.v1_12_R1.PlayerListBox;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Timer;

public class TabManager extends Manager {

    private Timer timer;
    private final long startTime = L2X9RebootCore.getInstance().getStartTime();
    private final DecimalFormat format = new DecimalFormat("##.##");
    private ConfigurationWrapper config;

    public TabManager() {
        super("TabList");
    }

    public ConfigurationWrapper getConfig() {
        return config;
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        timer = new Timer();
        config = IOUtil.createConfig(plugin, getName(), getName() + "-config", "configs/tab.yml");
        timer.scheduleAtFixedRate(new TabRunnable(this), 0, 1000);
    }

    @Override
    public void destruct(L2X9RebootCore plugin) {
        timer.cancel();
    }

    @Override
    public void reloadConfig(ConfigCreator creator) {
        config = creator.getConfigs().get(getName() + "-config");
    }

    public String parsePlaceHolders(String input, Player player) {
        String tps = String.valueOf(format.format(((CraftServer)Bukkit.getServer()).getHandle().getServer().recentTps[0]));
        int ping = getPing(player);
        return Utils.translateChars(input.
                replace("%tps%", Utils.getTPSColor(tps) + tps).
                replace("%players%", Bukkit.getOnlinePlayers().size() + "")).
                replace("%ping%", ping + "").
                replace("%uptime%", Utils.getFormattedInterval(System.currentTimeMillis() - startTime));
    }

    private int getPing(Player player) {
        try {
            String ver = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + ver + ".entity.CraftPlayer");
            Object handle = craftPlayer.getMethod("getHandle", new Class[0]).invoke(player);
            Field pingF = handle.getClass().getDeclaredField("ping");
            pingF.setAccessible(true);
            return (int) pingF.get(handle);
        } catch (Throwable t) {
            t.printStackTrace();
            return -1;
        }
    }
}
