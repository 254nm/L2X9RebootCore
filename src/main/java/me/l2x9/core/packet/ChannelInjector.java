package me.l2x9.core.packet;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.AllArgsConstructor;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class ChannelInjector extends ChannelDuplexHandler {
    private final PacketEventDispatcher dispatcher;
    private final Player player;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        try {
            PacketEvent.Outgoing outgoing = new PacketEvent.Outgoing((Packet<?>) msg, player);
            dispatcher.dispatch(outgoing);
            if (outgoing.isCancelled()) return;
            super.write(ctx, outgoing.getPacket(), promise);
        } catch (Throwable t) {
            Utils.log("A Packet listener had an exception");
            t.printStackTrace();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (!ctx.channel().isOpen()) return;
            PacketEvent.Incoming incoming = new PacketEvent.Incoming((Packet<?>) msg, player);
            dispatcher.dispatch(incoming);
            if (incoming.isCancelled()) return;
            super.channelRead(ctx, incoming.getPacket());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
