package me.l2x9.core.impl.patches.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import me.l2x9.core.L2X9RebootCore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class ChunkBan {
    public static void protocolLibWrapper(L2X9RebootCore plugin) {
        final Set<String> crafting = new HashSet<>();
        final Map<Player, Integer> levels = new HashMap<>();
        final Map<Player, Integer> boatLevels = new HashMap<>();
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(
                new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.MAP_CHUNK) {
                    @Override
                    public void onPacketSending(PacketEvent e) {
                        try {
                            PacketContainer packet = e.getPacket();
                            List list = packet.getSpecificModifier(List.class).read(0);
                                if (list.size() > 1024) {
                                    StructureModifier<Integer> ints = packet.getIntegers();
                                    StructureModifier<byte[]> byteArray = packet.getByteArrays();
                                    System.out.println(ChatColor.translateAlternateColorCodes('&', "&6[&3l2&bx9&6] " + e.getPlayer().getDisplayName() + " is in a chunk ban! Packet: " + e.getPacket().toString() + " Size: " + list.size()));
                                    packet.getSpecificModifier(List.class).writeDefaults();
                                }
                            } catch (Exception ex) {
                            System.out.println("Nigger");
                        }
                    }
                });
    }
}
