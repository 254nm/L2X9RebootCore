package me.l2x9.core.impl.chat.listeners;

import me.l2x9.core.impl.chat.ChatManager;
import me.l2x9.core.boiler.event.CustomEventHandler;
import me.l2x9.core.boiler.event.Listener;
import me.l2x9.core.boiler.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

import java.lang.reflect.Field;
import java.util.List;

public class AntiLink implements Listener {
    private final ChatManager manager;
    private Field messageF;

    public AntiLink(ChatManager manager) {
        this.manager = manager;
        try {
            messageF = PacketPlayOutChat.class.getDeclaredField("a");
            messageF.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @CustomEventHandler
    public void onPacket(PacketEvent.Outgoing event) {
        if (event.getPacket() instanceof PacketPlayOutChat) {
            try {
                PacketPlayOutChat packet = (PacketPlayOutChat) event.getPacket();
                IChatBaseComponent message = (IChatBaseComponent) messageF.get(packet);
                List<String> list = manager.getConfig().getConfig().getStringList("Blocked");
                for (String word : list) {
                    if (message.toPlainText().contains(word)) {
                        event.setCancelled(true);
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
