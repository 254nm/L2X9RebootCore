package me.l2x9.core.boiler.event.listener;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        inject(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        removeHook(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        removeHook(event.getPlayer());
    }


    private void inject(Player player) {
        EntityPlayer entity = ((CraftPlayer) player).getHandle();
        if (entity.playerConnection.networkManager.channel == null) return;
        ChannelPipeline pipeline = entity.playerConnection.networkManager.channel.pipeline();
        if (pipeline == null) return;
        pipeline.addBefore("packet_handler", "packet_listener", new NettyInjector(player));
    }

    private void removeHook(Player player) {
        EntityPlayer entity = ((CraftPlayer) player).getHandle();
        if (entity.playerConnection.networkManager.channel == null) return;
        Channel channel = entity.playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove("packet_listener");
        });
    }
}
