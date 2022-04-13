package me.l2x9.core.tablist;

import lombok.RequiredArgsConstructor;
import me.txmc.protocolapi.reflection.ClassProcessor;
import me.txmc.protocolapi.reflection.GetField;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

import java.lang.reflect.Field;
import java.util.TimerTask;

@RequiredArgsConstructor
public class TabRunnable extends TimerTask {
    private final TabManager manager;

    @GetField(clazz = PacketPlayOutPlayerListHeaderFooter.class, name = "a")
    private Field headerField;
    @GetField(clazz = PacketPlayOutPlayerListHeaderFooter.class, name = "b")
    private Field footerField;

    @Override
    public void run() {
        if  (headerField == null || footerField == null) ClassProcessor.process(this);
        Bukkit.getOnlinePlayers().forEach(player -> {
            try {
                String headerStr = String.join("\n", manager.getConfig().getStringList("Header"));
                String footerStr = String.join("\n", manager.getConfig().getStringList("Footer"));
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
