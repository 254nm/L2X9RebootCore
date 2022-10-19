package me.l2x9.core.patches.listeners;

import lombok.RequiredArgsConstructor;
import me.l2x9.core.patches.PatchManager;
import me.l2x9.core.util.Utils;
import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketListener;
import me.txmc.protocolapi.reflection.GetField;
import net.minecraft.server.v1_12_R1.PacketPlayInTabComplete;
import net.minecraft.server.v1_12_R1.PacketPlayOutTabComplete;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FakePlugins implements PacketListener {
    @GetField(clazz = PacketPlayInTabComplete.class, name = "a") private Field commandF;

    private final ConfigurationSection config;
    private final PatchManager ignored;

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
        PacketPlayInTabComplete packet = (PacketPlayInTabComplete) event.getPacket();
        String command = (String) commandF.get(packet);
        if (command.equals("/")) {
            Player player = event.getPlayer();
            PacketPlayOutTabComplete response = new PacketPlayOutTabComplete(formatPlugins());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(response);
            Utils.log("&3Prevented&r&a %s&r&3 from using .pl", player.getName());
            event.setCancelled(true);
        }
    }

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {
    }

    private String[] formatPlugins() {
        List<String> plugins = config.getStringList("PluginList");
        List<String> formatted = new ArrayList<>();
        for (String plugin : plugins) formatted.add("/" + plugin.concat(": "));
        return formatted.toArray(new String[0]);
    }
}
