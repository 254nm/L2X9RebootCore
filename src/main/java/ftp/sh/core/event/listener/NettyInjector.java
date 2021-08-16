package ftp.sh.core.event.listener;

import ftp.sh.core.L2X9RebootCore;
import ftp.sh.core.event.events.PacketEvent;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.entity.Player;

public class NettyInjector extends ChannelDuplexHandler {
    private final Player player;

    public NettyInjector(Player player) {
        this.player = player;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PacketEvent.Incoming incoming = new PacketEvent.Incoming((Packet<?>) msg, player);
        L2X9RebootCore.EVENT_BUS.post(incoming);
        if (incoming.isCancelled()) return;
        super.channelRead(ctx, incoming.getPacket());
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        PacketEvent.Outgoing outgoing = new PacketEvent.Outgoing((Packet<?>) msg, player);
        L2X9RebootCore.EVENT_BUS.post(outgoing);
        if (outgoing.isCancelled()) return;
        super.write(ctx, outgoing.getPacket(), promise);
    }
}
