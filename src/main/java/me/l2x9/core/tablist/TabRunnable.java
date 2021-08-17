package me.l2x9.core.tablist;

import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

import java.lang.reflect.Field;
import java.util.TimerTask;

public class TabRunnable extends TimerTask {
    private final TabManager manager;
    private Field headerField;
    private Field footerField;

    public TabRunnable(TabManager manager) {
        this.manager = manager;
        try {
            headerField = PacketPlayOutPlayerListHeaderFooter.class.getDeclaredField("a");
            headerField.setAccessible(true);
            footerField = PacketPlayOutPlayerListHeaderFooter.class.getDeclaredField("b");
            footerField.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            try {
                String headerStr = String.join("\n", manager.getConfig().getConfig().getStringList("Header"));
                String footerStr = String.join("\n", manager.getConfig().getConfig().getStringList("Footer"));
                IChatBaseComponent header = new ChatComponentText(manager.parsePlaceHolders(headerStr, player));
                IChatBaseComponent footer = new ChatComponentText(manager.parsePlaceHolders(footerStr, player));
                CraftPlayer craftPlayer = (CraftPlayer) player;
                PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
                headerField.set(packet, header);
                footerField.set(packet, footer);
                craftPlayer.getHandle().playerConnection.sendPacket(packet);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
