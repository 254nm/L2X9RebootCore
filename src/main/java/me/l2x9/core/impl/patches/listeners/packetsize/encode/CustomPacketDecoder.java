package me.l2x9.core.impl.patches.listeners.packetsize.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.*;

import java.io.IOException;
import java.util.List;

public class CustomPacketDecoder extends ByteToMessageDecoder {

    private final EnumProtocolDirection protocolDirection;

    public CustomPacketDecoder(EnumProtocolDirection var1) {
        this.protocolDirection = var1;
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        if (buf.readableBytes() != 0) {
            EnumProtocol protocol = ctx.channel().attr(NetworkManager.c).get();
            PacketDataSerializer serializer = new PacketDataSerializer(buf);
            int id = serializer.g();
            Packet<?> packet = protocol.a(protocolDirection, id);
            if (packet == null) throw new IOException("Bad packet id " + id);
            int len = serializer.readableBytes();
            int MAX_LEN = 2097152;
            if (len >= MAX_LEN) {
                serializer.clear();
                Utils.log(String.format("&aPrevented an extremely large&r&3 %s &r&apacket with length of&r&3 %d/%d &r&afrom being received&r", packet.getClass().getName(), len, MAX_LEN));
                return;
            }
            packet.a(serializer);
            if (serializer.readableBytes() > 0)
                throw new IOException("Packet " + protocol.a() + "/" + id + " (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + serializer.readableBytes() + " bytes extra whilst reading packet " + id);

        }
    }
}
