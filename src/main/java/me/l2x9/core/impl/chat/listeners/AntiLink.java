package me.l2x9.core.impl.chat.listeners;

import lombok.RequiredArgsConstructor;
import me.l2x9.core.impl.chat.ChatManager;
import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketListener;
import me.txmc.protocolapi.reflection.GetField;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

import java.lang.reflect.Field;
import java.util.List;

@RequiredArgsConstructor
public class AntiLink implements PacketListener {
    private final ChatManager manager;
    @GetField(clazz = PacketPlayOutChat.class, name = "a")
    private Field messageF;

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {

    }

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {
        if (event.getPacket() instanceof PacketPlayOutChat) {
            PacketPlayOutChat packet = (PacketPlayOutChat) event.getPacket();
            IChatBaseComponent message = (IChatBaseComponent) messageF.get(packet);
            if (message == null) return;
            List<String> list = manager.getConfig().getStringList("Blocked");
            for (String word : list) {
                if (message.toPlainText().contains(word)) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }
}
