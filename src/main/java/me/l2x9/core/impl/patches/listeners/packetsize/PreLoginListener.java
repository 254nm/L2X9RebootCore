package me.l2x9.core.impl.patches.listeners.packetsize;

import io.netty.channel.ChannelPipeline;
import me.l2x9.core.impl.patches.listeners.packetsize.compress.CustomPacketDecompressor;
import me.l2x9.core.impl.patches.listeners.packetsize.encode.CustomPacketDecoder;
import me.l2x9.core.impl.patches.listeners.packetsize.encode.CustomPacketEncoder;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.EnumProtocolDirection;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PreLoginListener implements Listener {

    private final MinecraftServer server;

    public PreLoginListener() {
        server = ((CraftServer) Bukkit.getServer()).getServer();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPreLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.replace("encoder", "encoder", new CustomPacketEncoder(player));
        if (pipeline.get("decompress") != null) {
            pipeline.replace("decompress", "decompress", new CustomPacketDecompressor(server.aG(), player));
        } else pipeline.replace("decode", "decode", new CustomPacketDecoder(EnumProtocolDirection.SERVERBOUND));
        System.out.println(pipeline);
        Utils.log("Added a custom packet encoder & decoder to " + event.getPlayer().getName());
    }
}
