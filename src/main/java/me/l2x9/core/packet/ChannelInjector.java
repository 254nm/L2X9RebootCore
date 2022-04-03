package me.l2x9.core.packet;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChannelInjector extends ChannelDuplexHandler {
    private final PacketEventDispatcher dispatcher;
    private final Player player;

    public ChannelInjector(PacketEventDispatcher dispatcher, Player player) {
        this.dispatcher = dispatcher;
        this.player = player;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        try {
            Bukkit.getServer().getScheduler().runTask(dispatcher.getPlugin(), () -> {
                PacketEvent.Outgoing outgoing = new PacketEvent.Outgoing((Packet<?>) msg, player);
                dispatcher.dispatch(outgoing);
                if (outgoing.isCancelled() || outgoing.getPacket() == null) return;
                try {
                    super.write(ctx, outgoing.getPacket(), promise);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });
        } catch (Throwable t) {
            Utils.log("A Packet listener had an exception");
            t.printStackTrace();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (!ctx.channel().isOpen()) return;
            Bukkit.getServer().getScheduler().runTask(dispatcher.getPlugin(), () -> {
                PacketEvent.Incoming incoming = new PacketEvent.Incoming((Packet<?>) msg, player);
                dispatcher.dispatch(incoming);
                if (incoming.isCancelled() || incoming.getPacket() == null) return;
                try {
                    super.channelRead(ctx, incoming.getPacket());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
