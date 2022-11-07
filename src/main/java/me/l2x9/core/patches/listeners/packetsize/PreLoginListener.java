package me.l2x9.core.patches.listeners.packetsize;

import io.netty.channel.ChannelPipeline;
import me.l2x9.core.patches.listeners.packetsize.encode.CustomPacketDecoder;
import me.l2x9.core.patches.listeners.packetsize.encode.CustomPacketEncoder;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.EnumProtocolDirection;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PreLoginListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        if (pipeline.get("encoder") == null || pipeline.get("decoder") == null) {
            Utils.kick(player, "kick_failed_to_inject");
        }
        pipeline.replace("encoder", "encoder", new CustomPacketEncoder(player));
        pipeline.replace("decoder", "decoder", new CustomPacketDecoder(EnumProtocolDirection.SERVERBOUND, player));
        System.out.println(pipeline);
        Utils.log("Added a custom packet encoder & decoder to " + event.getPlayer().getName());
    }
}
